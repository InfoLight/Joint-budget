package joint_budget.joint_budget.Events.Events;

import joint_budget.joint_budget.DataTypes.Event;
import joint_budget.joint_budget.DataTypes.User;

public interface EventsPresenterInterface {
    void createEvent(Event event);
    void joinEvent(String ID, String password);
    void addParticipantToEvent(User user, Event event);
    void deleteParticipantFromEvent(User user, Event event);
    void editEvent(Event event);
    void deleteEvent(Event event);
}