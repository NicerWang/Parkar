<template>
  <v-chart :loading="loadStatus" class="chart" :option="myOption" :autoresize="true"
           :style="{height:'400px',width:'100%'}"/>
</template>

<script>
import VChart, {THEME_KEY} from "vue-echarts";
import * as echarts from 'echarts/core';
import { use } from "echarts/core";
import { CanvasRenderer } from "echarts/renderers";
import { LineChart,BarChart } from "echarts/charts";
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  DataZoomComponent,
  DatasetComponent
} from "echarts/components";

use([
  DatasetComponent,
  CanvasRenderer,
  LineChart,
  BarChart,
  GridComponent,
  DataZoomComponent,
  TitleComponent,
  TooltipComponent,
  LegendComponent
]);

import {reactive, ref} from "vue";
import axios from "axios";
import {useStore} from "vuex";
export default {
  components:{
    VChart,
  },
  provide: {
    [THEME_KEY]: "light"
  },
  props:{
    id:Number
  },
  name: "chart",
  setup(props) {

    let store = useStore();
    let loadStatus = ref(true);
    let chartData = [];

    const colors = [
      ['rgba(171, 183, 255, 0.7)', 'rgba(189, 213, 255, 0.7)', 'rgba(224, 241, 255, 0.7)'],
      ['rgba(71, 71, 69, 0.7)', 'rgba(112, 111, 109, 0.7)', 'rgba(171, 171, 173, 0.7)'],
      ['rgba(211, 121, 132, 0.7)', 'rgba(234, 153, 164, 0.7)', 'rgba(251, 216, 217, 0.7)'],
      ['rgba(36, 90, 106, 0.7)', 'rgba(146, 189, 130, 0.7)', 'rgba(194, 230, 166, 0.7)']
    ];

    let myOption = reactive({
      tooltip: {
        // trigger: 'axis',
      },
      dataset: {
        dimensions: ['time', 'value'],
        source: []
      },
      xAxis: {
        type: 'time',
        boundaryGap: false,
        axisLabel: {
          formatter: function (value, index) {
            let date = new Date(value - 1);
            let y = date.getFullYear(),
                m = (date.getMonth() + 1) < 10 ? '0'+(date.getMonth() + 1) : (date.getMonth() + 1),
                d = date.getDate() < 10 ? '0'+date.getDate() : date.getDate();
            return y+'-'+m+'-'+d;
          },
        }
      },
      yAxis: {
        type: 'value',
        boundaryGap: [0, '100%']
      },
      dataZoom: [{
        start: 0,
        end: 100,
        minSpan: 20,
        backgroundColor: '#fff',
        fillerColor: colors[props.id][2],
      }],
      series: [
        {
          type: 'line',
          sampling: 'lttb',
          connectNulls: true,
          clip: false,
          itemStyle: {
            color: colors[props.id][0]
          },
          lineStyle: {
            width: 4
          }
        },
        {
          type: 'bar',
          sampling: 'lttb',
          connectNulls: true,
          clip: false,
          itemStyle: {
            color: colors[props.id][0]
          },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
              offset: 0,
              color: colors[props.id][1]
            }, {
              offset: 1,
              color: colors[props.id][2]
            }])
          }
        }
      ]
    });

    const update = function () {
      chartData.sort();
      myOption.dataset.source = chartData;
      store.dispatch("Finished")
      setTimeout(()=>{
        loadStatus.value = false;
      },1000)
    }

    axios({
      url: "admin/mr/" + props.id,
      headers: {'token': localStorage.getItem("token")},
    }).then((res) => {
      chartData = res.data;
      update();
    })

    return{
      loadStatus,
      myOption
    }
  }
}
</script>

<style scoped>

</style>