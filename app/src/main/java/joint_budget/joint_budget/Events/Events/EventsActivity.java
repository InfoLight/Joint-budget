package joint_budget.joint_budget.Events.Events;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import joint_budget.joint_budget.DataTypes.Event;
import joint_budget.joint_budget.Event.EventActivity;
import joint_budget.joint_budget.Events.Choice.ChoiceActivity;
import joint_budget.joint_budget.Events.CreateEvent.CreateEventActivity;
import joint_budget.joint_budget.R;

public class EventsActivity extends AppCompatActivity implements EventsView {

    private EventsAdapter eventsAdapter;
    private EventsPresenterInterface presenter;
    @BindView(R.id.events_add_event)
    FloatingActionButton createEvent;
    @BindView(R.id.events_listView)
    ListView eventsListView;
    @BindView(R.id.empty_events)
    TextView emptyEvents;
    @BindView(R.id.EventsProgressBar)
    ProgressBar progressBar;
    @BindView(R.id.swipeIssues)
    FrameLayout issues;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        try {
            initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initialize() throws IOException {
        ButterKnife.bind(this);
        presenter = new EventsPresenter(this, getApplicationContext());
        presenter.getCurrentUser(getIntent());
        presenter.loadEvents();
    }

    @Override
    public void onEventCreate(View view) {
        launchChoiceActivity();
    }

    private void launchChoiceActivity() {
        Intent intent = new Intent(getBaseContext(), ChoiceActivity.class);
        intent.putExtra("userID", presenter.getUserID());
        startActivity(intent);
    }

    @Override
    public void showEvents(final List<Event> events) {
        eventsAdapter = new EventsAdapter(this, R.layout.event_item, events, this);
        eventsListView.setEmptyView(emptyEvents);
        eventsListView.setAdapter(eventsAdapter);
        eventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                launchEventActivity(events.get(i));
            }
        });
    }

    @Override
    public void deleteEvent(Event event) {
        presenter.deleteEvent(event);
        eventsAdapter.notifyDataSetChanged();
    }

    @Override
    public void turnOnPrgressBar(){
        issues.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void turnOffPrgressBar(){
        issues.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void editEvent(Event event) {
        launchCreateEventActivity(event);
    }

    @Override
    public void updateListView() {
        eventsAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    public void launchCreateEventActivity(Event previousEvent){
        Intent intent = new Intent(getBaseContext(), CreateEventActivity.class);
        Gson gson = new Gson();
        String eventInJson = gson.toJson(previousEvent, Event.class);
        intent.putExtra("PreviousEvent", eventInJson);
        intent.putExtra("userID", presenter.getUserID());
        startActivity(intent);
    }

    public void launchEventActivity(Event event){
        Intent intent = new Intent(getBaseContext(), EventActivity.class);
        Gson gson = new Gson();
        String eventInJson = gson.toJson(event, Event.class);
        intent.putExtra("Event", eventInJson);
        intent.putExtra("userID", presenter.getUserID());
        startActivity(intent);
    }
}
