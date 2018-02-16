package trainedge.myapplication.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.androidviewhover.BlurLayout;
import com.google.android.gms.appinvite.AppInviteInvitation;

import trainedge.myapplication.R;
import trainedge.myapplication.activity.AboutActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link InviteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InviteFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int INVITE_REQUEST_CODE = 99;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private Button btn_invite;
    private Button btn_feedback;
    private Button btn_rate;
    private Button btn_about;
    private BlurLayout mSampleLayout;
    private BlurLayout mSampleLayout2;
    private BlurLayout mSampleLayout3;
    private BlurLayout mSampleLayout4;

    public InviteFragment() {
        // Required empty public constructor
    }
    public static InviteFragment newInstance(String param1, String param2) {
        InviteFragment fragment = new InviteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_invite, container, false);
        BlurLayout.setGlobalDefaultDuration(450);
        mSampleLayout = view.findViewById(R.id.blur_layout);
        View hover=LayoutInflater.from(getContext()).inflate(R.layout.hover_sample,null);
        hover.findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.Tada)
                        .duration(550)
                        .playOn(view);
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
            }
        });
        mSampleLayout.setHoverView(hover);
        mSampleLayout.setBlurDuration(550);
       // mSampleLayout.addChildAppearAnimator(hover, R.id.heart, Techniques.FlipInX, 550, 0);
        mSampleLayout.addChildAppearAnimator(hover, R.id.share, Techniques.FlipInX, 550, 250);
        //mSampleLayout.addChildAppearAnimator(hover, R.id.more, Techniques.FlipInX, 550, 500);

        //mSampleLayout.addChildDisappearAnimator(hover, R.id.heart, Techniques.FlipOutX, 550, 500);
        mSampleLayout.addChildDisappearAnimator(hover, R.id.share, Techniques.FlipOutX, 550, 250);
        //mSampleLayout.addChildDisappearAnimator(hover, R.id.more, Techniques.FlipOutX, 550, 0);

        mSampleLayout.addChildAppearAnimator(hover, R.id.description, Techniques.FadeInUp);
        mSampleLayout.addChildDisappearAnimator(hover, R.id.description, Techniques.FadeOutDown);
        mSampleLayout2 = view.findViewById(R.id.blur_layout2);
        View hover2=LayoutInflater.from(getContext()).inflate(R.layout.hover_sample2,null);
        mSampleLayout2.setHoverView(hover2);

        mSampleLayout2.addChildAppearAnimator(hover2, R.id.description, Techniques.FadeInUp);
        mSampleLayout2.addChildDisappearAnimator(hover2, R.id.description, Techniques.FadeOutDown);
        mSampleLayout2.addChildAppearAnimator(hover2, R.id.avatar, Techniques.DropOut, 1200);
        mSampleLayout2.addChildDisappearAnimator(hover2, R.id.avatar, Techniques.FadeOutUp);
        mSampleLayout2.setBlurDuration(1000);
        hover2.findViewById(R.id.avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent feedbackIntent = new Intent(android.content.Intent.ACTION_SEND);
                feedbackIntent.setType("text/html");
                feedbackIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{getString(R.string.mail_feedback_email)});
                feedbackIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.mail_feedback_subject));
                feedbackIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.mail_feedback_message));
                startActivity(Intent.createChooser(feedbackIntent, getString(R.string.title_send_feedback)));

            }
        });


        mSampleLayout3 = view.findViewById(R.id.blur_layout3);
        View hover3=LayoutInflater.from(getContext()).inflate(R.layout.hover_sample3,null);
        mSampleLayout3.setHoverView(hover3);
        mSampleLayout3.addChildAppearAnimator(hover3, R.id.ic_star_border_black_24dp, Techniques.Landing);
        mSampleLayout3.addChildDisappearAnimator(hover3, R.id.ic_star_border_black_24dp, Techniques.TakingOff);
        mSampleLayout3.enableZoomBackground(true);
        mSampleLayout3.setBlurDuration(1200);
        hover3.findViewById(R.id.ic_star_border_black_24dp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.Tada)
                        .duration(550)
                        .playOn(view);
                 startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=trainedge.myapplication.fragment")));

            }
        });


        mSampleLayout4 = view.findViewById(R.id.blur_layout4);
        View hover4=LayoutInflater.from(getContext()).inflate(R.layout.hover_sample4,null);
        mSampleLayout4.setHoverView(hover4);
        mSampleLayout4.addChildAppearAnimator(hover4, R.id.ic_person_outline_black_24dp, Techniques.SlideInLeft);
        mSampleLayout4.addChildAppearAnimator(hover4, R.id.mail, Techniques.SlideInRight);

        mSampleLayout4.addChildDisappearAnimator(hover4, R.id.ic_person_outline_black_24dp, Techniques.SlideOutLeft);
        mSampleLayout4.addChildDisappearAnimator(hover4, R.id.mail, Techniques.SlideOutRight);

        mSampleLayout4.addChildAppearAnimator(hover4, R.id.content, Techniques.BounceIn);
        mSampleLayout4.addChildDisappearAnimator(hover4, R.id.content, Techniques.FadeOutUp);

        hover4.findViewById(R.id.ic_person_outline_black_24dp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent1);
            }
        });
        hover4.findViewById(R.id.mail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"divyanganakothari1996@gmail.com"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "About the Application/ Any Suggestion");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I have a good idea about this project..");

                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
        });


        return view;
    }

  }
