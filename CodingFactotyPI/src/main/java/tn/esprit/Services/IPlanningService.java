package tn.esprit.Services;

import tn.esprit.entities.Event;
import tn.esprit.entities.LocationEvent;
import tn.esprit.entities.Planning;

import java.util.List;

public interface IPlanningService {

    public Planning addPlanning(Planning planning, Long idEvent, Long idLocationEvent);
    public Planning updatePlanning(Planning planning,Long idLocationEvent);
    public Planning retrievePlanning(Long idPlanning);
    public void deletePlanning(Long idPlanning);
    public List<Planning> getPlanning();
    
}
