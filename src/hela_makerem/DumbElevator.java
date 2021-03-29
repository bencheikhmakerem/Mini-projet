package hela_makerem;

import model.Person;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class DumbElevator implements Elevator {

    private static final String UP = "UP";
    private static final String DOWN = "down";
    private Random random = new Random();

    private int elevatorCapacity;
	private int currentFloor;
	private List<List<Person>> peopleByFloor;
	private List<Person> listPeopleInElevator = new ArrayList<>();

	private boolean lastPersonArrived = false;

	private int peopleInElevator = 0;
	private int peopleWaitingAtFloors = 0;
    private String direction;
	private int[] waitingAtFloor;

    public DumbElevator(int elevatorCapacity) {
        this.elevatorCapacity = elevatorCapacity;
        this.direction = UP;
    }

    @Override
    public void startsAtFloor(LocalTime time, int initialFloor) {
		this.currentFloor = initialFloor;
    }

    @Override
    public void peopleWaiting(List<List<Person>> peopleByFloor) {
		this.peopleByFloor = peopleByFloor;
		this.peopleWaitingAtFloors = 
				peopleByFloor.stream()
							.mapToInt(List::size)
							.sum();
		
		this.waitingAtFloor = peopleByFloor
								.stream()
								.mapToInt(t -> t.size())
								.toArray();
		for(int i =0 ; i < 4 ; i++)
			System.out.println( waitingAtFloor[i]);
		
    }

    @Override
    public int chooseNextFloor() {
    	
    	if (lastPersonArrived && 
    			this.peopleInElevator == 0 && 
    			this.peopleWaitingAtFloors == 0) {
    		return 1;
    	} else {
    	    if (Objects.equals(this.direction, UP) && this.currentFloor == 4) {
    	        this.direction = DOWN;
            }
            if (Objects.equals(this.direction, DOWN) && this.currentFloor == 1) {
                this.direction = UP;
            }
    	    if (Objects.equals(this.direction, UP)) {
    	    
    	    	if (lastPersonArrived && this.peopleInElevator == 0) {
    	    		//=> il ya des personnes qui attendent => chercher les dernieres personnes qui attendent
    	    	    if (noOneIsWaitingAtCurrentFloor()) {
    	                return findFloorUpNext();
    	            } else {
    	    	        return this.peopleByFloor.get((this.currentFloor)- 1).get(0).getDestinationFloor();
    	            }
    	        } 
    	    	else 
    	    		if (lastPersonArrived) {
    	    			//peopleInElevator != 0 => decharger l'assenceur
    	    			return this.listPeopleInElevator.get(0).getDestinationFloor();
    	    		} 
    	    		else {
	    	            if (peopleWaitingAtFloors == 0) {
	    	                return currentFloor;
	    	            }
	    	            List<Integer> emptyFloors = findEmptyFloorsDifferentFromCurrent();
	    	            if (emptyFloors.isEmpty()) {
	    	                return findFloorUpNext();
	    	            } else {
	    	                return emptyFloors.get(0);
    	            }
    	    	}
    	    }
    	    else {
            	
    	    	if (lastPersonArrived && this.peopleInElevator == 0) {
    	    		//=> il ya des personnes qui attendent => chercher les dernieres personnes qui attendent
    	    	    if (noOneIsWaitingAtCurrentFloor()) {
    	                return findFloorDownNext();
    	            } else {
    	    	        return this.peopleByFloor.get((this.currentFloor)- 1).get(0).getDestinationFloor();
    	            }
    	        } 
    	    	else 
    	    		if (lastPersonArrived) {
    	    			//peopleInElevator != 0 => decharger l'assenceur
    	    			return this.listPeopleInElevator.get(0).getDestinationFloor();
    	    		} 
    	    		else {
	    	            if (peopleWaitingAtFloors == 0) {
	    	                return currentFloor;
	    	            }
	    	            List<Integer> emptyFloors = findEmptyFloorsDifferentFromCurrent();
	    	            if (emptyFloors.isEmpty()) {
	    	                return findFloorDownNext();
	    	            } else {
	    	                return emptyFloors.get(0);
    	            }
    	    	}
            }
    	}
    }
    
    //retorune liste des étages vides
    private List<Integer> findEmptyFloorsDifferentFromCurrent() {
        List<Integer> emptyFloorsDifferentFromCurrent = new ArrayList<>();
        for (int floorIndex = 0 ; floorIndex < this.peopleByFloor.size() ; floorIndex++) {
            if (floorIndex != (this.currentFloor - 1) && this.peopleByFloor.get(floorIndex).isEmpty()) {
                emptyFloorsDifferentFromCurrent.add(floorIndex + 1);
            }
        }
        return emptyFloorsDifferentFromCurrent;
    }
    
    private boolean noOneIsWaitingAtCurrentFloor() {
        return this.peopleByFloor.get(this.currentFloor - 1).isEmpty();
    }

    private int findFloorUpNext() {
    	for (int floorIndex = currentFloor+1 ; floorIndex <= 4 ; floorIndex++) {
            int numberOFPeopleWaiting = this.peopleByFloor.get(floorIndex-1).size();
            if (numberOFPeopleWaiting > 0 ) {
                return floorIndex;
            }
        }
    	return 1;
    }
    
    private int findFloorDownNext() {
    	for (int floorIndex = currentFloor-1 ; floorIndex <= 1 ; floorIndex++) {
            int numberOFPeopleWaiting = this.peopleByFloor.get(floorIndex-1).size();
            if (numberOFPeopleWaiting > 0 ) {
                return floorIndex;
            }
        }
    	return 1;
    }
    
    @Override
    public void arriveAtFloor(int floor) {
    	this.currentFloor = floor;
    }

    @Override
    public void loadPerson(Person person) {
    	this.peopleInElevator++;
    	this.peopleWaitingAtFloors--;
    	this.peopleByFloor.get(this.currentFloor - 1).remove(person);
    	this.waitingAtFloor= peopleByFloor
								.stream()
								.mapToInt(t -> t.size())
								.toArray();
    	for(int i =0 ; i < 4 ; i++)
			System.out.println( waitingAtFloor[i]);
    	this.listPeopleInElevator.add(person);
    }

    @Override
    public void unloadPerson(Person person) {
    	this.peopleInElevator--;
    	this.listPeopleInElevator.remove(person);
    }

    @Override
    public void newPersonWaitingAtFloor(int floor, Person person) {
    	this.peopleWaitingAtFloors++;
    	this.peopleByFloor.get(floor - 1).add(person);
    	this.waitingAtFloor= peopleByFloor
							.stream()
							.mapToInt(t -> t.size())
							.toArray();
    	for(int i =0 ; i < 4 ; i++)
			System.out.println( waitingAtFloor[i]);
    }

    @Override
    public void lastPersonArrived() {
    	System.out.println("**********is the last person**********");
    	this.lastPersonArrived  = true;
    }
}