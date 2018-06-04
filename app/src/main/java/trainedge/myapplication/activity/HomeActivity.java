package trainedge.myapplication.activity;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;
import trainedge.myapplication.R;
import trainedge.myapplication.fragment.ContactsFragment;
import trainedge.myapplication.fragment.ContactsToChatFragment;
import trainedge.myapplication.fragment.InviteFragment;

import static trainedge.myapplication.R.id.ntb;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int INVITE_REQUEST_CODE = 99;
    public ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wrapper_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(ntb);
        viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        // initUI();

        final String[] colors = getResources().getStringArray(R.array.default_preview);

        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_person_outline_black_24dp),
                        Color.parseColor(colors[0]))
                        .title("Contacts")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_chat_black_24dp),
                        Color.parseColor(colors[1]))
                        .title("Chat")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_share_black_24dp),
                        Color.parseColor(colors[2]))
                        .title("Invite")
                        .build()
        );

        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 1);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        int count = navigationView.getHeaderCount();
        if (count == 1) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            View header = navigationView.getHeaderView(0);
            TextView tv_name= header.findViewById(R.id.tv_name);
            TextView tv_email= header.findViewById(R.id.tv_email);
            ImageView ivMain = header.findViewById(R.id.iv_main);

            if (user != null) {
                Uri photoUrl = user.getPhotoUrl();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Glide.with(this).load(photoUrl).into(ivMain).onLoadFailed(getDrawable(R.drawable.ic_person_outline_black_24dp));
                }
                String email = user.getEmail();
                String name = user.getDisplayName();
                tv_name.setText(name);
                tv_email.setText(email);

            }
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
          viewPager.setCurrentItem(0);

        } else if (id == R.id.nav_gallery) {
            viewPager.setCurrentItem(1);

        } else if (id == R.id.nav_manage) {
            viewPager.setCurrentItem(2);



        }
        else if (id == R.id.nav_share) {
            try {
                Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                        .setMessage("Join your friends")
                        .setDeepLink(Uri.parse("/link"))
                        .setCallToActionText(getString(R.string.invitation_cta))
                        .build();
                startActivityForResult(intent, INVITE_REQUEST_CODE);
            } catch (ActivityNotFoundException ac) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                //sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.sharing_book_title, bookTitle));
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }

        } else if (id == R.id.nav_send) {
            Intent feedbackIntent = new Intent(android.content.Intent.ACTION_SEND);
            feedbackIntent.setType("text/html");
            feedbackIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{getString(R.string.mail_feedback_email)});
            feedbackIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.mail_feedback_subject));
            feedbackIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.mail_feedback_message));
            startActivity(Intent.createChooser(feedbackIntent, getString(R.string.title_send_feedback)));

        }
        else if (id== R.id.log_out){
            AlertDialog.Builder builder=new AlertDialog.Builder(HomeActivity.this); //Home is name of the activity
            builder.setMessage("Do you want to Log Out?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });


            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            AlertDialog alert=builder.create();
            alert.show();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ContactsFragment.newInstance("", "");
                case 1:
                    return ContactsToChatFragment.newInstance("", "");
                case 2:
                    return InviteFragment.newInstance("", "");
            }

            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

   /* private void initUI() {

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 3;
            }
            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }
            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                final View view = LayoutInflater.from(
                        getBaseContext()).inflate(R.layout.item_vp_list, null, false);

                final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(
                                getBaseContext(), LinearLayoutManager.VERTICAL, false
                        )
                );
                recyclerView.setAdapter(new RecycleAdapter());

                container.addView(view);
                return view;
            }
        });



        navigationTabBar.post(new Runnable() {
            @Override
            public void run() {
                final View viewPager = findViewById(R.id.vp_horizontal_ntb);
                ((ViewGroup.MarginLayoutParams) viewPager.getLayoutParams()).topMargin =
                        (int) -navigationTabBar.getBadgeMargin();
                viewPager.requestLayout();
            }
        });
        navigationTabBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(final NavigationTabBar.Model model, final int index) {

            }

            @Override
            public void onEndTabSelected(final NavigationTabBar.Model model, final int index) {
                model.hideBadge();
            }
        });

        findViewById(R.id.mask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            final String title = String.valueOf(new Random().nextInt(15));
                            if (!model.isBadgeShowed()) {
                                model.setBadgeTitle(title);
                                model.showBadge();
                            } else model.updateBadgeTitle(title);
                        }
                    }, i * 100);
                }
            }
        });
    }
    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            final View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.item_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.txt.setText(String.format("Navigation Item #%d", position));
        }
        @Override
        public int getItemCount() {
            return 20;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView txt;

            public ViewHolder(final View itemView) {
                super(itemView);
                txt = (TextView) itemView.findViewById(R.id.txt_vp_item_list);
            }
        }
    }*/

}
