package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class NumbersFragment extends Fragment {

    private MediaPlayer mMediaplayer;

    private AudioManager mAudioManager;

    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {

                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                            focusChange == AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK) {
                        //pause playback
                        mMediaplayer.pause();
                        mMediaplayer.seekTo(0);
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        //resume playback
                        mMediaplayer.start();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {

                        //stop playback
                        releaseMediaPlayer();
                    }

                }
            };


    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };


    public NumbersFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> words = new ArrayList<Word>();



        words.add(new Word("One", "एकम्", R.drawable.number_one, R.raw.one_number));
        words.add(new Word("Two", "द्वे", R.drawable.number_two, R.raw.two_number));
        words.add(new Word("Three", "त्रीणि", R.drawable.number_three, R.raw.three_number));
        words.add(new Word("Four", "चत्वारि", R.drawable.number_four, R.raw.four_number));
        words.add(new Word("Five", "पञ्च", R.drawable.number_five, R.raw.five_number));
        words.add(new Word("Six", "षट्", R.drawable.number_six, R.raw.six_number));
        words.add(new Word("Seven", "सप्त", R.drawable.number_seven, R.raw.seven_number));
        words.add(new Word("Eight", "अष्ट", R.drawable.number_eight, R.raw.eight_number));
        words.add(new Word("Nine", "नव", R.drawable.number_nine, R.raw.nine_number));
        words.add(new Word("Ten", "दश", R.drawable.number_ten, R.raw.ten_number));

        WordAdapter Adapter = new WordAdapter(getActivity(), words, R.color.category_numbers);

        ListView listView = (ListView) rootView.findViewById(R.id.list);

        listView.setAdapter(Adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Word word = words.get(position);

                releaseMediaPlayer();

                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener, AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                    mMediaplayer = MediaPlayer.create(getActivity(), word.getAudioResourceId());
                    mMediaplayer.start();
                    mMediaplayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();

        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        if (mMediaplayer != null) {
            mMediaplayer.release();

            mMediaplayer = null;

            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);

        }
    }

}

