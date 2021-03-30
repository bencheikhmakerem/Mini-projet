// Hela DHIFLI && Makerem BEN CHEIKH

package hela_makerem;
import model.Person;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ABetterElevator implements Elevator {

    private static final String UP = "UP";
    private static final String DOWN = "down";

	private int currentFloor;
	private List<List<Person>> peopleByFloor;
	private List<Person> listPeopleInElevator = new ArrayList<>();

	private boolean lastPersonArrived = false;

	private int peopleInElevator = 0;
	private int peopleWaitingAtFloors = 0;
    private String direction;

    public ABetterElevator(int elevatorCapacity) {
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
    }

    @Override
    public int chooseNextFloor() {
    	
    	if (lastPersonArrived && 
    			this.peopleInElevator == 0 && 
    			this.peopleWaitingAtFloors == 0) {
    		return 1;
    	}
    	else {
    	    if (Objects.equals(this.direction, UP) && this.currentFloor == 4) {
    	        this.direction = DOWN;
            }
            if (Objects.equals(this.direction, DOWN) && this.currentFloor == 1) {
                this.direction = UP;
            }
    	    if (Objects.equals(this.direction, UP)) {
    	    
    	    	if (lastPersonArrived && this.peopleInElevator == 0) {
    	    	    if (noOneIsWaitingAtCurrentFloor()) {
    	                return findFloorUpNext();
    	            } else {
    	    	        return this.peopleByFloor.get((this.currentFloor)- 1).get(0).getDestinationFloor();
    	            }
    	        } 
    	    	else 
    	    		if (lastPersonArrived) {
    	            	System.out.println("*" +lastPersonArrived + " " + this.peopleWaitingAtFloors + " " + this.peopleInElevator);
    	    			return this.listPeopleInElevator.get(0).getDestinationFloor();
    	    		} 
    	    		else {
    	            	if (peopleWaitingAtFloors == 0) {
	    	                return currentFloor;
	    	            }
	    	            return findFloorUpNext();
    	    		}
    	    	
    	    }
    	    else {
            	
    	    	if (lastPersonArrived && this.peopleInElevator == 0) {
    	    	    if (noOneIsWaitingAtCurrentFloor()) {
    	                return findFloorDownNext();
    	            } else {
    	    	        return this.peopleByFloor.get((this.currentFloor)- 1).get(0).getDestinationFloor();
    	            }
    	        } 
    	    	else 
    	    		if (lastPersonArrived) {
    	    			return this.listPeopleInElevator.get(0).getDestinationFloor();
    	    		} 
    	    		else {
    	    	    	
    	    			if (peopleWaitingAtFloors == 0) {
	    	                return currentFloor;
    	    			}
	    	            return findFloorDownNext();
    	            
    	    	}
            }
    	}
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
    	return 4;
    }
    
    private int findFloorDownNext() {
    	for (int floorIndex = currentFloor-1 ; floorIndex > 0 ; floorIndex--) {
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
    }

    @Override
    public void lastPersonArrived() {
    	System.out.println("*is the last person*");
    	this.lastPersonArrived  = true;
    }
}