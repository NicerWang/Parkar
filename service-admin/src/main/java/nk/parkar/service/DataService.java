package nk.parkar.service;

import nk.parkar.pojo.DataItem;

import java.util.ArrayList;
import java.util.Date;

public interface DataService {
    boolean insert(DataItem dataItem);
    ArrayList<DataItem> getAllByTimeInterval(Date start, Date end);
}
