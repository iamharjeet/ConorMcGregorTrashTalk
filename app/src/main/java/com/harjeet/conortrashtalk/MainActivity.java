package com.harjeet.conortrashtalk;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private DividerItemDecoration mDividerItemDecoration;
    private ArrayList<Talk> mData;

    private ArrayList<Talk> talkArrayList;
    private MyAdapter filterAdapter;
    private MenuItem mMenuItem;

    //TODO: Uncomment following line to implement search functionality
//    private EditText inputSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: Uncomment following line to implement search functionality
//        inputSearch = (EditText) findViewById(R.id.inputSearch);


        mData = new ArrayList<>();
        initData();

        Collections.sort(mData, new Comparator<Talk>() {
            @Override
            public int compare(Talk o1, Talk o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }

        });

        mRecyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MyAdapter(mData, this);
        filterAdapter = (MyAdapter) mAdapter;
        mRecyclerView.setAdapter(mAdapter);

        mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                1);
        mDividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.line_divider));
        mRecyclerView.addItemDecoration(mDividerItemDecoration);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);



        FloatingActionButton fab = findViewById(R.id.fab);

        // Playing random audio
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random random_method = new Random();
                int index = random_method.nextInt(mData.size());
                AudioPlay.stopAudio();
                int milli = AudioPlay.playAudioRandom(MainActivity.this, mData.get(index).getUrl());

               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        //TODO: Uncomment following code to implement search functionality
        /*if(inputSearch!=null){
            inputSearch.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    int textlength = cs.length();
                    ArrayList<Talk> tempArrayList = new ArrayList<Talk>();
                    for(Talk c: mData){
                        if (textlength <= c.getTitle().length()) {
                            if (c.getTitle().toLowerCase().contains(cs.toString().toLowerCase())) {
                                tempArrayList.add(c);
                            }
                        }
                    }
                    mAdapter = new MyAdapter(tempArrayList, MainActivity.this);
                    mRecyclerView.setAdapter(mAdapter);

                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
                }
            });
        }*/

    }

    @Override
    protected void onPause() {
        AudioPlay.stopAudio();
        super.onPause();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // inside inflater we are inflating our menu file.
        inflater.inflate(R.menu.search_menu, menu);

        // below line is to get our menu item.
        MenuItem searchItem = menu.findItem(R.id.actionSearch);
        mMenuItem = searchItem;
        // getting search view of our item.
        SearchView searchView = (SearchView) searchItem.getActionView();


        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // inside on query text change method we are
                // calling a method to filter our recycler view.

                filter(newText);
                return false;
            }
        });
        /*searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    searchView.setIconified(true);
                }
            }
        });*/

        return true;
    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<Talk> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (Talk item : mData) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getTitle().toLowerCase().contains(text.toLowerCase()) || item.getDesc().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if ( filteredlist.isEmpty()  ) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "Nothing Matched..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            filterAdapter.filterList(filteredlist);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                /*Intent intent = new Intent(this, SupportActivity.class);
                startActivity(intent);*/
                showDialog();
                // do something based on first item click
                return true;
            case R.id.share:
                //TODO: uncomment this code
                try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Conor Mcgregor Trash Talk");
                String sAux = "Hey! Checkout this app with the largest collection of Conor Mcgregor trash talks!\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=com.harjeet.conortrashtalk";
                i.putExtra(Intent.EXTRA_TEXT, sAux);

                startActivity(Intent.createChooser(i, "Share"));
            } catch(Exception e) {
                e.toString();
            }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public void onBackPressed() {
        SearchView searchView = (SearchView) mMenuItem.getActionView();
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        } else {
            super.onBackPressed();
        }
    }*/

    public void showDialog(){

        /*Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

// inflate and adjust layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(this.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_dialog_about, null);
        layout.setMinimumWidth((int)(displayRectangle.width() * 0.9f));*/

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog_about);

        Button dialogButton = dialog.findViewById(R.id.btn_ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void initData() {

        mData.add(new Talk("60Gs Baby", "Conor's UFC Debut, UFC on Fuel TV: Mousasi vs. Latifi", R.raw.sixtygs));
        mData.add(new Talk("Bum Life", "UFC 197 Press Conference, McGregor to Rafael dos Anjos", R.raw.bumlife));
        mData.add(new Talk("Red Panty Night", "UFC 197 Press Conference, McGregor to Rafael dos Anjos", R.raw.redpanty));
        mData.add(new Talk("I'm the money fight", "UFC 197 Press Conference, McGregor to Rafael dos Anjos", R.raw.moneyfight));
        mData.add(new Talk("I back it up", "UFC 197 Press Conference, McGregor to Rafael dos Anjos", R.raw.backitup));
        mData.add(new Talk("I am boxing", "Mayweather vs. McGregor World Tour", R.raw.iamboxing));
        mData.add(new Talk("Nate can only count to five", "UFC 196 TV Interview", R.raw.natecountfive));
        mData.add(new Talk("Stomp on his head", "UFC 229 Khabib vs. McGregor Press Conference", R.raw.stomponhishead));
        mData.add(new Talk("I own Rio de Janeiro", "UFC 194 Aldo vs. McGregor Press Conference", R.raw.iownriodejaneiro));
        mData.add(new Talk("You little fan boy", "UFC 229 Khabib vs. McGregor Press Conference", R.raw.fanboy));
        mData.add(new Talk("That little rat", "UFC 229 Khabib vs. McGregor Press Conference", R.raw.littlerat));
        mData.add(new Talk("I'd like to apologize", "UFC 205: Alvarez vs. McGregor Post Fight", R.raw.apologize));
        mData.add(new Talk("Double champ does what he wants", "UFC 205: Alvarez vs. McGregor Post Fight", R.raw.doublechamp));
        mData.add(new Talk("I run New York City", "UFC 205: Alvarez vs. McGregor Press Conference", R.raw.irunnewyorkcity));
        mData.add(new Talk("I am a pimp", "UFC 205: Alvarez vs. McGregor Press Conference", R.raw.iamapimp));
        mData.add(new Talk("Fook the Mayweathers", "Mayweather vs. McGregor World Tour", R.raw.fookmayweathers));
        mData.add(new Talk("Who da fook", "UFC 205: Alvarez vs. McGregor Press Conference", R.raw.whodafook));
        mData.add(new Talk("Fook Showtime", "Mayweather vs. McGregor World Tour", R.raw.fookshowtime));
        mData.add(new Talk("Be Me or Fight Me", "UFC 196 TV Interview", R.raw.beme));
        mData.add(new Talk("Sorry I'm late", "UFC 205: Alvarez vs. McGregor Press Conference", R.raw.iamlate));
        mData.add(new Talk("You mad backward c*nt", "UFC 229 Khabib vs. McGregor Press Conference", R.raw.youcunt));
        mData.add(new Talk("Fooking dead", "UFC 229 Khabib vs. McGregor Press Conference", R.raw.youdead));
        mData.add(new Talk("You little weasel", "Mayweather vs. McGregor World Tour", R.raw.youweasel));
        mData.add(new Talk("You are blessed", "UFC 205: Alvarez vs. McGregor Press Conference", R.raw.youareblessed));
        mData.add(new Talk("Fook Mark Henry", "UFC 205: Alvarez vs. McGregor Press Conference", R.raw.fookmarkhenry));
        mData.add(new Talk("I'll cut you like a fish", "UFC 196 TV Interview McGregor to Nate", R.raw.cutyoulikefish));
        mData.add(new Talk("Unhealthy obsession", "Conor in a car", R.raw.obsession));
        mData.add(new Talk("You won't do shit", "Mayweather vs. McGregor World Tour", R.raw.youwontdoshit));
        mData.add(new Talk("Here's my location", "UFC 229 Khabib vs. McGregor Press Conference", R.raw.mylocation));
        mData.add(new Talk("You know what that means", "Conor to Jose Aldo", R.raw.youknowhatthatmeans));
        mData.add(new Talk("Big Irish balls", "UFC 205: Alvarez vs. McGregor Press Conference", R.raw.bigballs));
        mData.add(new Talk("Shut your fooking mouth", "UFC 205: Alvarez vs. McGregor Press Conference", R.raw.shutyourmouth));
        mData.add(new Talk("I run new york", "UFC 205: Alvarez vs. McGregor Press Conference", R.raw.irunnewyork));
        mData.add(new Talk("I operate on my time", "UFC 205: Alvarez vs. McGregor Press Conference", R.raw.ioperate));
        mData.add(new Talk("You can't even read", "Mayweather vs. McGregor World Tour", R.raw.youcantread));
        mData.add(new Talk("I'll tap your head off the canvas", "UFC 229 Khabib vs. McGregor Press Conference", R.raw.tapyourhead));
        mData.add(new Talk("Do something about it", "UFC 229 Khabib vs. McGregor Press Conference", R.raw.dosomethingaboutit));
        mData.add(new Talk("Started from the bottom", "Mayweather vs. McGregor World Tour", R.raw.startedfromthebottom));
        mData.add(new Talk("Ask deez nuts", "UFC 229 Khabib vs. McGregor Press Conference", R.raw.deeznuts));
        mData.add(new Talk("Net worth", "UFC 202 Diaz vs. McGregor 2 Press Conference", R.raw.networth));
        mData.add(new Talk("Mystic Mac", "UFC 178 McGregor vs. Poirier Post Fight", R.raw.mysticmac));
        mData.add(new Talk("Sit down", "Mayweather vs. McGregor World Tour", R.raw.sitdown));
        mData.add(new Talk("Good girl", "Mayweather vs. McGregor World Tour", R.raw.goodgirl));
        mData.add(new Talk("You'll dew nuttin", "UFC 202 Diaz vs. McGregor 2 Press Conference", R.raw.donothin));
        mData.add(new Talk("Get the fook outa here", "UFC 202 Diaz vs. McGregor 2 Press Conference", R.raw.getthefook));
        mData.add(new Talk("Fook you", "UFC 202 Diaz vs. McGregor 2 Press Conference", R.raw.fookyou));
        mData.add(new Talk("My socks", "UFC 196 TV Interview, McGregor to Nate ", R.raw.mysocks));
        mData.add(new Talk("His facial structure will be rearranged", "UFC 189 Mendes vs. McGregor Press Conference", R.raw.hisfacialstructure));
        mData.add(new Talk("I like the kid", "UFC 178 McGregor vs. Poirier Press Conference", R.raw.ilikethekid));
        mData.add(new Talk("He's a p*ssy", "UFC Fight Night: McGregor vs. Siver Press Conference", R.raw.hesapussy));
        mData.add(new Talk("I'll hire him", "UFC 194: Aldo vs. McGregor Press Conference", R.raw.illhirehim));
        mData.add(new Talk("This is not a therapy session", "McGregor and Aldo Interview", R.raw.therapysession));
        mData.add(new Talk("Without me, he's prelim", "McGregor and Aldo Interview", R.raw.hesprelim));
        mData.add(new Talk("Brown Belt", "UFC 178 McGregor vs. Poirier Post Fight presser", R.raw.brownbelt));
        mData.add(new Talk("Fook show business", "UFC 189: Aldo vs. McGregor Press Conference", R.raw.fookshowbusiness));
        mData.add(new Talk("I wouldn't fight me either", "McGregor Interview", R.raw.iwouldntfightme));
        mData.add(new Talk("I can rest my balls on your forehead", "McGregor to Mendes", R.raw.restmyballs));
        mData.add(new Talk("Tell Jose I'm comin", "UFC Fight Night: McGregor vs. Siver Weigh Ins", R.raw.telljose));
        mData.add(new Talk("I need a body to show up", "UFC 189: Conor McGregor Interview", R.raw.ineedabody));
        mData.add(new Talk("It's my time", "UFC 178 Conor McGregor Interview", R.raw.itsmytime));
        mData.add(new Talk("Give me the weight and the date", "Conor McGregor Fox Sports Interview", R.raw.givemeweight));
        mData.add(new Talk("I'll collect heads", "UFC 178 McGregor vs. Poirier Press Conference presser", R.raw.collectheads));
        mData.add(new Talk("I'm gonna cut him in half", "UFC 189 McGregor vs. Mendes Weigh Ins", R.raw.cuthiminhalf));
        mData.add(new Talk("I'm a specimen", "Conor McGregor UFC Interview", R.raw.iamaspecimen));
        mData.add(new Talk("Call me Jose Senior", "UFC 189 McGregor and Aldo Interview", R.raw.josesenior));
        mData.add(new Talk("Your little midget head", "UFC 179 McGregor to Mendes", R.raw.midgethead));
        mData.add(new Talk("These custom made suits aren't cheap", "UFC Fight Night: Shogun vs. Sonnen, Press Conference", R.raw.custommadesuit));
        mData.add(new Talk("Tree People", "UFC Fight Night: Shogun vs. Sonnen, Post Fight Press Conference", R.raw.treepeople));
        mData.add(new Talk("I'm the whale", "Conor McGregor interview with Brendan Schaub", R.raw.iamthewhale));
        mData.add(new Talk("Me and Jesus", "UFC 196 McGregor vs. dos Anjos Press Conference", R.raw.meandjesus));
        mData.add(new Talk("Gods Recognize Gods", "UFC 196 McGregor vs. dos Anjos Press Conference", R.raw.gods));
        mData.add(new Talk("I only accept money in briefcases", "McGregor interview", R.raw.briefcases));
        mData.add(new Talk("Don't talk about my money", "UFC 178 McGregor vs. Poirier Post Fight presser", R.raw.mymoney));
        mData.add(new Talk("Mcgregor Era", "UFC 189 McGregor and Aldo Interview", R.raw.mcgregorera));
        mData.add(new Talk("Fook Off", "McGregor in a car", R.raw.fookoff));
        mData.add(new Talk("Every Year", "UFC 196 McGregor vs. dos Anjos Press Conference", R.raw.everyyear));
        mData.add(new Talk("I love you", "UFC 194: Aldo vs. McGregor Press Conference", R.raw.iloveyou));
        mData.add(new Talk("Gimme the belt", "Cage Warriors: Fight Night 2 Post Fight", R.raw.givemethebelt));
        mData.add(new Talk("Kiss my feet", "Conor McGregor promo", R.raw.kissmyfeet));
        mData.add(new Talk("Dollar Bill signs", "Ariel Helwani interview", R.raw.dollarbillsigns));
        mData.add(new Talk("Brazillian Weasel", "UFC 194: Aldo vs. McGregor Press Conference", R.raw.brazillianweasel));
        mData.add(new Talk("Left hand", "UFC 194: Aldo vs. McGregor Post fight", R.raw.lefthand));
        mData.add(new Talk("Precision beats power", "UFC 194: Aldo vs. McGregor Post fight", R.raw.precision));
        mData.add(new Talk("Da-da-da-da-dahh", "McGregor cycling", R.raw.dadadadada));
        mData.add(new Talk("My employees making more money", "Ariel Helwani interview", R.raw.myemployees));
        mData.add(new Talk("The whole America can hate me", "Conor McGregor interview", R.raw.benjaminfranklin));
        mData.add(new Talk("The Mcgregor Show", "UFC 189 Press Conference", R.raw.mcgregorshow));
        mData.add(new Talk("Mystic Mac strikes again", "UFC 194: Aldo vs. McGregor Post Fight Press Conference", R.raw.mysticmacstrikesagain));
        mData.add(new Talk("Viva la brasilia", "UFC 196 McGregor vs. dos Anjos Press Conference", R.raw.vivalabrasilia));
        mData.add(new Talk("Bum soul", "McGregor in a car", R.raw.bumsouls));
        mData.add(new Talk("C in UFC", "Conor McGregor interview", R.raw.cinufc));
        mData.add(new Talk("I'm comin for the whole company", "Conor McGregor interview", R.raw.iamcomingforthecompany));
        mData.add(new Talk("Gynecologist", "Conor McGregor on Conan, talking about Jose Aldo", R.raw.gynecologist));
        mData.add(new Talk("League of my own", "UFC 196 McGregor vs. dos Anjos Press Conference", R.raw.leagueofmyown));
        mData.add(new Talk("Not on my level", "UFC 196 McGregor vs. dos Anjos Press Conference", R.raw.notonmylevel));
        mData.add(new Talk("Pay me royalties", "Conor McGregor interview with Brendan Schaub", R.raw.paymeroyalties));
        mData.add(new Talk("I'm number 1,2,3,4...", "Conor McGregor interview", R.raw.iamnumber1));
        mData.add(new Talk("Eat his carcass", "UFC 196 McGregor vs. Diaz Press Conference", R.raw.carcass));
        mData.add(new Talk("Animal Balloons", "UFC 196 McGregor vs. Diaz Press Conference", R.raw.animalballoons));
        mData.add(new Talk("Super Heavyweight", "UFC 196 McGregor vs. Diaz Press Conference", R.raw.heavyweight));
        mData.add(new Talk("They all have sore vag", "UFC 196 McGregor vs. Diaz Press Conference", R.raw.sorevag));
        mData.add(new Talk("Big ball sack", "UFC 205: Alvarez vs. McGregor TV Interview", R.raw.bigballsack));
        mData.add(new Talk("Fook Floyd Mayweather", "Ariel Helwani Interview", R.raw.fookfloyd));
        mData.add(new Talk("Come kiss my feet", "UFC 194: Aldo vs. McGregor Brazil Promotion", R.raw.comekissmyfeet));
        mData.add(new Talk("Where's my money", "Conor Mcgregor Promo", R.raw.wheresmymoney));
        mData.add(new Talk("N.O.T.O.R.I.O.U.S", "Mayweather vs. McGregor World Tour", R.raw.notorious));
        mData.add(new Talk("Rip his head off", "Mayweather vs. McGregor Promo", R.raw.riphisheadoff));
        mData.add(new Talk("I own Rio", "UFC 194: Aldo vs. McGregor Brazil Promotion", R.raw.iownrio));
        mData.add(new Talk("Hand on his forehead", "UFC 194: Aldo vs. McGregor Press Conference", R.raw.handonhisforehead));
        mData.add(new Talk("I remove a head", "Conor McGregor Press Conference", R.raw.removeahead));
        mData.add(new Talk("Tree Elephants", "UFC 178 McGregor vs. Poirier Post Fight presser", R.raw.treeelephants));
        mData.add(new Talk("I don't care about his skin", "UFC 189: Aldo vs. McGregor Press Conference", R.raw.hisskin));
        mData.add(new Talk("I should run for president of the US", "UFC 189: Aldo vs. McGregor Post Fight", R.raw.presidentofus));
        mData.add(new Talk("My weigh ins generate more gate", "Pre Fight preparation in the arena", R.raw.myweighins));
        mData.add(new Talk("Be prepared to die", "UFC 189: Aldo vs. McGregor Press Conference", R.raw.bepreparedtodie));
        mData.add(new Talk("Dance for me nate", "UFC 196 McGregor vs. Diaz Press Conference", R.raw.danceforme));
        mData.add(new Talk("Don't talk about money you broke", "UFC 196 McGregor vs. Diaz Press Conference", R.raw.donttalkaboutmoney));
        mData.add(new Talk("Current king", "Interview with Ariel Helwani", R.raw.currentking));
        mData.add(new Talk("We are here to take over", "Fight Night Dublin: Post Fight", R.raw.takeover));
        mData.add(new Talk("They are dweebs", "Mcgregor on WWE wrestlers", R.raw.theyaredweebs));
        mData.add(new Talk("Mouthy fools", "UFC 246: Post Fight", R.raw.mouthyfools));
        mData.add(new Talk("I'll eliminate you two in 45 seconds", "Mcgregor ESPN Interview", R.raw.eliminateyou));
        mData.add(new Talk("I'm always humble", "UFC 178 McGregor vs. Poirier Post Fight presser", R.raw.iamhumble));
        mData.add(new Talk("As long as there's money", "UFC 178 McGregor vs. Poirier Post Fight presser", R.raw.aslongastheresmoney));
        mData.add(new Talk("Truck loads of cash", "UFC 178 McGregor vs. Poirier Post Fight presser", R.raw.truckloads));
        mData.add(new Talk("It's never personal", "UFC 178 McGregor vs. Poirier Post Fight presser", R.raw.itsneverpersonal));
        mData.add(new Talk("These featherweights", "UFC 178 McGregor vs. Poirier Post Fight", R.raw.thesefeatherweights));
        mData.add(new Talk("We all go to war", "UFC 178 McGregor vs. Poirier Post Fight", R.raw.weallgotowar));
        mData.add(new Talk("Chad Mini Mendes", "UFC 178 McGregor vs. Poirier Post Fight", R.raw.chadminimendes));
        mData.add(new Talk("I pick the round", "UFC 178 McGregor vs. Poirier Post Fight", R.raw.ipicktheround));
        mData.add(new Talk("You're stiff as a board", "UFC 189: Go Big Press Conference, McGregor to Cowboy", R.raw.stiffasaboard));
        mData.add(new Talk("Featherweights hit like flyweights", "UFC 189: Press Conference", R.raw.featherweightshitlikeflyweights));
        mData.add(new Talk("Have I been wrong yet", "UFC 189: Press Conference", R.raw.guesswhat));
        mData.add(new Talk("Yeehaw", "UFC 189: Go Big Press Conference, McGregor to Cowboy", R.raw.yeehaw));
        mData.add(new Talk("My foot was a ballewn", "Conor McGregor to Ariel Helwani Interview", R.raw.ballewn));
        mData.add(new Talk("You are like a woman with that phone", "Conor McGregor to Tyron Woodley backstage", R.raw.likeawoman));
        mData.add(new Talk("WWE guys are p*ssies", "Conor McGregor TMZ interview", R.raw.wweguys));
        mData.add(new Talk("I tink I'm pound for pound No. 1", "Conor McGregor TMZ interview", R.raw.poundforpound));
        mData.add(new Talk("I tink I'm the greatest", "Conor McGregor TMZ interview", R.raw.iamgreatest));
        mData.add(new Talk("I butchered your face", "UFC 189: Go Big Press Conference, McGregor to Chad Mendes", R.raw.butcheredyourface));
        mData.add(new Talk("I ko'd you", "UFC 189: Go Big Press Conference, McGregor to Chad Mendes", R.raw.ikodyou));
        mData.add(new Talk("Hit the deck like a b*tch", "UFC 189: Go Big Press Conference, McGregor to Chad Mendes", R.raw.hitthedeck));
        mData.add(new Talk("Change your bum life as well", "UFC 189: Go Big Press Conference, McGregor to Cowboy", R.raw.bumlifeaswell));
        mData.add(new Talk("I can make you rich", "UFC 189: Go Big Press Conference, McGregor to dos Anjos", R.raw.makeyourich));
        mData.add(new Talk("Surprise Surprise", "UFC 202 Diaz vs. McGregor 2 Post fight", R.raw.surprise));
        mData.add(new Talk("I don't give a fook either", "UFC 196 McGregor vs. Diaz Press Conference", R.raw.givefookeither));
        mData.add(new Talk("Cholo Gangster", "UFC 196 McGregor vs. Diaz Press Conference", R.raw.chologangster));
        mData.add(new Talk("Watches", "UFC 196 McGregor vs. dos Anjos Press Conference", R.raw.watches));
        mData.add(new Talk("El Chapo", "UFC 196 McGregor vs. dos Anjos Press Conference", R.raw.elchapo));
        mData.add(new Talk("Desert", "UFC 196 McGregor vs. dos Anjos Press Conference", R.raw.desert));
        mData.add(new Talk("Bum Version of Aldo", "UFC 196 McGregor vs. dos Anjos Press Conference", R.raw.bumaldo));
        mData.add(new Talk("They all dress like me", "UFC 205: Alvarez vs. McGregor Press Conference", R.raw.dressedlikeme));
        mData.add(new Talk("This guy's a ballewn", "UFC 205: Alvarez vs. McGregor Press Conference", R.raw.thisguysaballewn));
        mData.add(new Talk("Irish are back", "UFC 205: Alvarez vs. McGregor Press Conference", R.raw.irishareback));
        mData.add(new Talk("And", "And", R.raw.and)); // UFC 189 Mendes vs. McGregor Press Conference
        mData.add(new Talk("They're all the same", "UFC 189 Mendes vs. McGregor Press Conference", R.raw.theyareallsame));

        // New data added December 6, 2021
        mData.add(new Talk("Aim For Peace", "UFC 229 Pre-fight Press Conference", R.raw.aimforpeace));
        mData.add(new Talk("Ali Abdelaziz", "UFC 229 Pre-fight Press Conference", R.raw.aliabdelaziz));
        mData.add(new Talk("Cheap motherf*rs", "UFC 205 Octagon Interview", R.raw.cheapmother));
        mData.add(new Talk("I'm gonna dance on your head", "To Dustin UFC 264 Pre-fight Press Conference", R.raw.danceonhead));
        mData.add(new Talk("Fook him", "To Dustin Poirer UFC 264 Octagon Interview", R.raw.fookhim));
        mData.add(new Talk("Fook peace", "UFC 229 Pre-fight Press Conference", R.raw.fookpeace));
        mData.add(new Talk("Fook the whole roster", "UFC 264 Pre-fight Press Conference", R.raw.fookroster));
        mData.add(new Talk("Fook you", "To Ali UFC 229 Pre-fight Press Conference", R.raw.fookyouali));
        mData.add(new Talk("Heavy detail", "UFC 229 Pre-fight Press Conference", R.raw.heavydetail));
        mData.add(new Talk("Hows Noah", "To Ali UFC 229 Pre-fight Press Conference", R.raw.howsnoah));
        mData.add(new Talk("I am no celebrity", "Interview with Stephen A Smith", R.raw.iamnocelebrity));
        mData.add(new Talk("I will wipe my tears with my money", "UFC 189 Promotion", R.raw.wipetears));
        mData.add(new Talk("I'd still whoop his ass", "For Jesus in a TMZ Interview", R.raw.idwhoophisass));
        mData.add(new Talk("Not a man alive", "TMZ Interview", R.raw.notamanalive));
        mData.add(new Talk("Out on a stretcher", "For Dustin Poirer Interview with Stephen A Smith", R.raw.outonastretcher));
        mData.add(new Talk("Shut your mouth", "To Ali UFC 229 Pre-fight Press Conference", R.raw.shutyourmouthali));
        mData.add(new Talk("Snake in the grass", "For TJ Dillashaw in the ultimate fighter", R.raw.snakeingrass));
        mData.add(new Talk("Them belts are in me gaff", "TMZ Interview", R.raw.thembelts));
        mData.add(new Talk("This is not over", "UFC 264 Octagon Interview", R.raw.thisisnotover));
        mData.add(new Talk("Where the fook is my second belt", "UFC 205 Octagon Interview", R.raw.secondbelt));
        mData.add(new Talk("You little hoe", "To Dustin Poirer's wife UFC 264 Octagon Interview", R.raw.youlittlehoe));
        mData.add(new Talk("You only a little bitch", "To Dustin UFC 264 Pre-fight Press Conference", R.raw.youonlybitch));
        mData.add(new Talk("Your wife is in me dms", "To Dustin Poirer UFC 264 Octagon Interview", R.raw.yourwife));

    }




}
