package joint_budget.joint_budget.API.FIrebaseAPI;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import joint_budget.joint_budget.API.DebtsAPI;
import joint_budget.joint_budget.DataTypes.Debt;

public class FirebaseDebtsAPI implements DebtsAPI {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public FirebaseDebtsAPI() {
        firebaseDatabase = FirebaseDatabase.getInstance("https://joint-budget-f59f7.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public void getAllDebts(final LoadDebtsCallback callback, String userID) {
        DatabaseReference referenceToEvents = databaseReference.child("debts");
        Query allDebts = referenceToEvents.orderByChild("debtParticipant1").equalTo(userID);
        allDebts.orderByChild("debtParticipant2").equalTo(userID);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Debt> debts = new ArrayList<>();
                for (DataSnapshot debtsSnapshot : dataSnapshot.getChildren()) {
                    debts.add(debtsSnapshot.getValue(Debt.class));
                }
                callback.onLoad(debts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw new RuntimeException();
            }
        };

        allDebts.addListenerForSingleValueEvent(eventListener);
    }

    @Override
    public void sendInvoice(String debtID) {

    }

    @Override
    public void markAsReturned(String debtID) {
        databaseReference.child("debts").child(debtID).removeValue();
    }

    @Override
    public String createDebt(Debt debt) {
        DatabaseReference referenceToDebts = databaseReference.child("debts");
        String key = referenceToDebts.push().getKey();
        referenceToDebts.child(key).setValue(debt);
        return key;
    }
}
