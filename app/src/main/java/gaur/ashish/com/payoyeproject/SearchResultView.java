package gaur.ashish.com.payoyeproject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import gaur.ashish.com.payoyeproject.model.MovieFields;

/**
 * Created by Ashish Gaur on 5/13/2016.
 */
public class SearchResultView extends AppCompatActivity {

    Bitmap bitmap;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    private MovieFields mf = new MovieFields();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_page);


        final ImageView img = (ImageView) findViewById(R.id.result_img);
        new ImageLoadTask(MovieFields.getPoster(), img).execute();

        TextView name = (TextView) findViewById(R.id.result_title_txt);
        TextView year = (TextView) findViewById(R.id.result_year_txt);
        TextView actors = (TextView) findViewById(R.id.result_actors_txt);
        TextView runtime = (TextView) findViewById(R.id.result_runtime_txt);
        TextView imdbRating = (TextView) findViewById(R.id.result_rating_txt);
        TextView plot = (TextView) findViewById(R.id.result_plot_txt);
        TextView genre = (TextView) findViewById(R.id.result_genre_txt);
        TextView released = (TextView) findViewById(R.id.result_released_txt);

        name.setText(MovieFields.getTitle());
        year.setText(MovieFields.getYear());
        actors.setText(MovieFields.getActors());
        runtime.setText(MovieFields.getRuntime());
        imdbRating.setText(MovieFields.getImdbRating());
        plot.setText(MovieFields.getPlot());
        genre.setText(MovieFields.getGenre());
        released.setText(MovieFields.getReleased());

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomImageFromThumb(img, bitmap);
            }
        });

        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
    }

    private void applyPalette(Palette palette) {
        int primary = getResources().getColor(R.color.colorPrimary);
        int primaryDark = getResources().getColor(R.color.colorPrimaryDark);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(palette.getMutedColor(primary)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(palette.getDarkVibrantColor(primary));
            getWindow().setNavigationBarColor(palette.getDarkMutedColor(primaryDark));
        }
        supportStartPostponedEnterTransition();
    }

    private void zoomImageFromThumb(final View thumbView, Bitmap imageResId) {
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        final ImageView expandedImage = (ImageView) findViewById(R.id.imageView);
        expandedImage.setImageBitmap(imageResId);

        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container).getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        thumbView.setAlpha(0f);
        expandedImage.setVisibility(View.VISIBLE);

        expandedImage.setPivotX(0f);
        expandedImage.setPivotY(0f);

        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImage, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImage, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImage, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImage,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        final float startScaleFinal = startScale;
        expandedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImage, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImage,
                                        View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImage,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImage,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImage.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImage.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
            if (result != null) {
                Palette.from(result).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        applyPalette(palette);
                    }
                });
                bitmap = result;
            }
        }
    }
}
