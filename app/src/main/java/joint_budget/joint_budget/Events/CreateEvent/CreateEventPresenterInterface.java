package joint_budget.joint_budget.Events.CreateEvent;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import joint_budget.joint_budget.DataTypes.User;

public interface CreateEventPresenterInterface {
    void setCurrentDate();
    void saveEvent(String name, String startDate, String finalDate,
                   String currency) throws ParseException, IOException;
    ArrayList<User> getUsers();
    void addNewParticipant(String username, String participantLinkOrPhone, ParticipantsAdapter participantsAdapter);
}
