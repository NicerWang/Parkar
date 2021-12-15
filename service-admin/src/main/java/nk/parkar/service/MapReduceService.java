package nk.parkar.service;

import nk.parkar.pojo.MRActivity;
import nk.parkar.pojo.MRAvail;
import nk.parkar.pojo.MRCancellation;
import nk.parkar.pojo.MRReservation;

import java.util.ArrayList;

public interface MapReduceService {

    int mapReduce();
    ArrayList<MRActivity> getActivityList();
    ArrayList<MRAvail> getAvailList();
    ArrayList<MRCancellation> getCancellationList();
    ArrayList<MRReservation> getReservationList();
}
