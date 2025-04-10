package io.github.fakebrotato;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Main extends ApplicationAdapter {
    private enum GameState {
        PLAYING,
        GAME_OVER
    }

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private Guardian guardian;
    private ArrayList<Enemy> enemies;
    private ArrayList<Bullet> playerBullets;
    private ArrayList<Bullet> bossBullets;
    private Boss boss;

    private Texture backgroundTexture;
    private Music backgroundMusic;
    private float spawnTimer;
    private float timeElapsed;
    private Random random;

    private float baseEnemySpeed = 150;
    private float speedIncrementRate = 10;

    private int enemiesDestroyed = 0;
    private final int enemiesToSpawnBoss = 10;
    private float bossInitialHealth = 1000;
    private boolean bossActive = false;

    private int playerHealth = 5;
    private final int maxPlayerHealth = 5;
    private int score = 0;

    private GameState gameState = GameState.PLAYING;
    private ArrayList<Integer> highScores = new ArrayList<>();
    private GameOverScreen gameOverScreen;

    @Override
    public void create() {
        try {
            batch = new SpriteBatch();
            shapeRenderer = new ShapeRenderer();
            font = new BitmapFont();
            font.getData().setScale(1.5f);
            guardian = new Guardian("guardian.png",
                Gdx.graphics.getWidth() / 2f - 50,
                100,
                0.5f);
            enemies = new ArrayList<>();
            playerBullets = new ArrayList<>();
            bossBullets = new ArrayList<>();
            backgroundTexture = new Texture("background.jpg");

            backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background_music.mp3"));
            backgroundMusic.setLooping(true);
            backgroundMusic.setVolume(0.5f);
            backgroundMusic.play();

            spawnTimer = 0;
            timeElapsed = 0;
            random = new Random();

            loadHighScores();
        } catch (Exception e) {
            Gdx.app.error("Create Error", "Error in create: " + e.getMessage(), e);
            Gdx.app.exit();
        }
    }

    private void loadHighScores() {
        Preferences prefs = Gdx.app.getPreferences("HighScores");
        highScores.clear();
        for (int i = 0; i < 10; i++) {
            int score = prefs.getInteger("score" + i, 0);
            if (score > 0) {
                highScores.add(score);
            }
        }
        Collections.sort(highScores, Collections.reverseOrder());
    }

    private void saveHighScores() {
        highScores.add(score);
        Collections.sort(highScores, Collections.reverseOrder());
        if (highScores.size() > 10) {
            highScores = new ArrayList<>(highScores.subList(0, 10));
        }

        Preferences prefs = Gdx.app.getPreferences("HighScores");
        for (int i = 0; i < highScores.size(); i++) {
            prefs.putInteger("score" + i, highScores.get(i));
        }
        prefs.flush();
    }

    private void resetGame() {
        guardian = new Guardian("guardian.png",
            Gdx.graphics.getWidth() / 2f - 50,
            100,
            0.5f);
        enemies.clear();
        playerBullets.clear();
        bossBullets.clear();
        if (boss != null) {
            boss.dispose();
            boss = null;
        }
        bossActive = false;
        enemiesDestroyed = 0;
        playerHealth = 5;
        score = 0;
        spawnTimer = 0;
        timeElapsed = 0;
        gameState = GameState.PLAYING;
        if (gameOverScreen != null) {
            gameOverScreen.dispose();
            gameOverScreen = null;
        }
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        timeElapsed += delta;

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (gameState == GameState.PLAYING) {
            guardian.update(delta);

            if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.SPACE)) {
                float bulletWidth = Bullet.getBulletWidth(0.5f);
                float bulletStartX = guardian.getX() + (guardian.getWidth() / 2) - (bulletWidth / 2);
                playerBullets.add(new Bullet(
                    bulletStartX,
                    guardian.getY() + guardian.getHeight(),
                    0.5f,
                    500,
                    0
                ));
            }

            if (!bossActive) {
                spawnTimer += delta;
                if (spawnTimer >= 1.5f) {
                    float enemyX = random.nextFloat() * (Gdx.graphics.getWidth() - 50);
                    float enemySpeed = baseEnemySpeed + timeElapsed * speedIncrementRate;
                    enemies.add(new Enemy("enemy.png", enemyX, Gdx.graphics.getHeight(), 0.5f, enemySpeed));
                    spawnTimer = 0;
                }
            } else if (boss != null) {
                boss.update(delta, bossBullets);
            }

            if (!bossActive && enemiesDestroyed >= enemiesToSpawnBoss) {
                boss = new Boss(
                    Gdx.graphics.getWidth() / 2f - 100,
                    Gdx.graphics.getHeight() - 150,
                    0.5f,
                    100,
                    bossInitialHealth
                );
                bossActive = true;
                enemies.clear();
            }

            batch.begin();
            batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            guardian.draw(batch);

            for (int i = 0; i < enemies.size(); i++) {
                Enemy enemy = enemies.get(i);
                enemy.update(delta);
                if (enemy.getY() < -enemy.getHeight()) {
                    enemies.remove(i);
                    i--;
                } else {
                    enemy.draw(batch);
                }
            }

            for (int i = 0; i < playerBullets.size(); i++) {
                Bullet bullet = playerBullets.get(i);
                bullet.update(delta);
                if (bullet.isOffScreen(Gdx.graphics.getHeight())) {
                    playerBullets.remove(i);
                    i--;
                } else {
                    bullet.draw(batch);
                }
            }

            for (int i = 0; i < bossBullets.size(); i++) {
                Bullet bullet = bossBullets.get(i);
                bullet.update(delta);
                if (bullet.getY() < -bullet.getHeight()) {
                    bossBullets.remove(i);
                    i--;
                } else {
                    bullet.draw(batch);
                }
            }

            if (bossActive && boss != null) {
                boss.draw(batch);
            }

            font.draw(batch, "Score: " + score, 10, Gdx.graphics.getHeight() - 10);
            batch.end();

            float healthBarWidth = 150;
            float healthBarHeight = 20;
            float healthRatio = (float) playerHealth / maxPlayerHealth;
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.GRAY);
            shapeRenderer.rect(10, Gdx.graphics.getHeight() - 50, healthBarWidth, healthBarHeight);
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.rect(10, Gdx.graphics.getHeight() - 50, healthBarWidth * healthRatio, healthBarHeight);
            shapeRenderer.end();

            if (bossActive && boss != null) {
                boss.drawHealthBar(shapeRenderer);
            }

            for (int i = 0; i < playerBullets.size(); i++) {
                Bullet bullet = playerBullets.get(i);
                boolean bulletRemoved = false;

                for (int j = 0; j < enemies.size(); j++) {
                    Enemy enemy = enemies.get(j);
                    if (enemy.isColliding(bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight())) {
                        playerBullets.remove(i);
                        enemies.remove(j);
                        enemiesDestroyed++;
                        score += 10;
                        i--;
                        bulletRemoved = true;
                        break;
                    }
                }

                if (!bulletRemoved && bossActive && boss != null && boss.isColliding(bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight())) {
                    boss.takeDamage(100);
                    playerBullets.remove(i);
                    i--;

                    if (boss.isDead()) {
                        score += 200;
                        boss.dispose();
                        boss = null;
                        bossActive = false;
                        enemiesDestroyed = 0;
                        bossInitialHealth *= 1.1f;
                    }
                }
            }

            for (int i = 0; i < enemies.size(); i++) {
                Enemy enemy = enemies.get(i);
                if (enemy.isColliding(guardian.getX(), guardian.getY(), guardian.getWidth(), guardian.getHeight())) {
                    enemies.remove(i);
                    playerHealth--;
                    i--;
                    if (playerHealth <= 0) {
                        saveHighScores();
                        gameState = GameState.GAME_OVER;
                        gameOverScreen = new GameOverScreen(score, highScores, () -> Gdx.app.exit(), this::resetGame);
                    }
                }
            }

            if (bossActive && boss != null && boss.isColliding(guardian.getX(), guardian.getY(), guardian.getWidth(), guardian.getHeight())) {
                boss.takeDamage(boss.getMaxHealth() * 0.3f);
                if (boss.isDead()) {
                    score += 200;
                    boss.dispose();
                    boss = null;
                    bossActive = false;
                    enemiesDestroyed = 0;
                    bossInitialHealth *= 1.1f;
                }
            }

            for (int i = 0; i < bossBullets.size(); i++) {
                Bullet bullet = bossBullets.get(i);
                if (bullet.isColliding(guardian.getX(), guardian.getY(), guardian.getWidth(), guardian.getHeight())) {
                    bossBullets.remove(i);
                    playerHealth--;
                    i--;
                    if (playerHealth <= 0) {
                        saveHighScores();
                        gameState = GameState.GAME_OVER;
                        gameOverScreen = new GameOverScreen(score, highScores, () -> Gdx.app.exit(), this::resetGame);
                    }
                }
            }
        } else if (gameState == GameState.GAME_OVER && gameOverScreen != null) {
            gameOverScreen.render();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
        guardian.dispose();
        for (Enemy enemy : enemies) {
            enemy.dispose();
        }
        for (Bullet bullet : playerBullets) {
            bullet.dispose();
        }
        for (Bullet bullet : bossBullets) {
            bullet.dispose();
        }
        if (boss != null) {
            boss.dispose();
        }
        if (gameOverScreen != null) {
            gameOverScreen.dispose();
        }
        backgroundTexture.dispose();
        backgroundMusic.dispose();
    }
}
