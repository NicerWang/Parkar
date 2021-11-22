package nk.parkar.service.impl;

import nk.parkar.mapper.DataMapper;
import nk.parkar.pojo.DataItem;
import nk.parkar.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
public class DataServiceImpl implements DataService {
    private DataMapper dataMapper;

    @Autowired
    public void setDataMapper(DataMapper dataMapper) {
        this.dataMapper = dataMapper;
    }

    @Override
    public boolean insert(DataItem dataItem) {
        return dataMapper.insert(dataItem) == 1;
    }

    @Override
    public ArrayList<DataItem> getAllByTimeInterval(Date start, Date end) {
        return (ArrayList<DataItem>) dataMapper.getAllByTimeInterval(start,end);
    }
}
