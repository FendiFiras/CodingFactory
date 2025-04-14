package tn.esprit.Services;

import tn.esprit.entities.LocationEvent;

import java.util.List;

public interface ILocationEventService {


    public LocationEvent addLocationEvent(LocationEvent locationEvent) ;

    public LocationEvent updateLocationEvent(LocationEvent locationEvent) ;

    public LocationEvent retrieveLocationEvent(Long idLocationEvent);

    public void deleteLocationEvent(Long idLocationEvent);
    public List<LocationEvent> getLocationEvent();

}
