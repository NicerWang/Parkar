package nk.parkar.management.service;

public interface ParkingBuilderService {
    void insertNoneSpace();

    Integer getMaxSpaceId();

    void updateCoordinate(Integer spaceId, Integer xCoordinate, Integer yCoordinate);
}
