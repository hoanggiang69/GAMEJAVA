package io.github.fakebrotato;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class GameOverScreen {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private int score;
    private ArrayList<Integer> highScores;
    private Runnable onExit;
    private Runnable onRestart;

    // Vị trí và kích thước nút
    private final float exitButtonX;
    private final float exitButtonY;
    private final float exitButtonWidth = 80;
    private final float exitButtonHeight = 40;

    private final float restartButtonX;
    private final float restartButtonY;
    private final float restartButtonWidth = 80;
    private final float restartButtonHeight = 40;

    public GameOverScreen(int score, ArrayList<Integer> highScores, Runnable onExit, Runnable onRestart) {
        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
        this.font = new BitmapFont();
        this.score = score;
        this.highScores = highScores;
        this.onExit = onExit;
        this.onRestart = onRestart;

        // Tính toán vị trí nút dựa trên kích thước màn hình
        exitButtonX = Gdx.graphics.getWidth() / 2f - 100;
        exitButtonY = Gdx.graphics.getHeight() / 2f - 150; // Đặt nút thấp hơn
        restartButtonX = Gdx.graphics.getWidth() / 2f + 20;
        restartButtonY = Gdx.graphics.getHeight() / 2f - 150; // Đặt nút thấp hơn
    }

    public void render() {
        batch.begin();
        // Hiển thị "Game Over" và điểm số
        font.getData().setScale(2f);
        font.draw(batch, "Game Over", Gdx.graphics.getWidth() / 2f - 80, Gdx.graphics.getHeight() / 2f + 150);
        font.draw(batch, "Score: " + score, Gdx.graphics.getWidth() / 2f - 50, Gdx.graphics.getHeight() / 2f + 100);

        // Hiển thị bảng điểm
        font.getData().setScale(1.5f);
        font.draw(batch, "High Scores:", Gdx.graphics.getWidth() / 2f - 60, Gdx.graphics.getHeight() / 2f + 50);
        for (int i = 0; i < highScores.size(); i++) {
            font.draw(batch, (i + 1) + ". " + highScores.get(i), Gdx.graphics.getWidth() / 2f - 40, Gdx.graphics.getHeight() / 2f + 20 - i * 30);
        }
        batch.end();

        // Vẽ nút Exit và Restart
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(exitButtonX, exitButtonY, exitButtonWidth, exitButtonHeight);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(restartButtonX, restartButtonY, restartButtonWidth, restartButtonHeight);
        shapeRenderer.end();

        batch.begin();
        font.getData().setScale(1f);
        font.draw(batch, "Exit", exitButtonX + 20, exitButtonY + 25);
        font.draw(batch, "Restart", restartButtonX + 10, restartButtonY + 25);
        batch.end();

        // Xử lý nhấn nút
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (touchX >= exitButtonX && touchX <= exitButtonX + exitButtonWidth &&
                touchY >= exitButtonY && touchY <= exitButtonY + exitButtonHeight) {
                onExit.run();
            }

            if (touchX >= restartButtonX && touchX <= restartButtonX + restartButtonWidth &&
                touchY >= restartButtonY && touchY <= restartButtonY + restartButtonHeight) {
                onRestart.run();
            }
        }
    }

    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
    }
}
