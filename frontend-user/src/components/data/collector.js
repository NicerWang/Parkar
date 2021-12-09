import axios from "axios";

export function collect(type, sa1, sa2, sa3, sa4) {
    axios({
        url:"/admin/data/" + type,
        params:{
            sa1:sa1,
            sa2:sa2,
            sa3:sa3,
            sa4:sa4
        },
        headers: {'token': localStorage.getItem("token")},
    })
}