package io.github.fakebrotato;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Random;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Guardian guardian;
    private ArrayList<Enemy> enemies;
    private ArrayList<Bullet> bullets;

    private Texture backgroundTexture;
    private float spawnTimer;
    private float timeElapsed;
    private Random random;

    private float baseEnemySpeed = 150;
    private float speedIncrementRate = 10;

    @Override
    public void create() {
        batch = new SpriteBatch();
        guardian = new Guardian("guardian.png",
            Gdx.graphics.getWidth() / 2f - 50,
            100,
            0.5f);
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        backgroundTexture = new Texture("background.jpg");
        spawnTimer = 0;
        timeElapsed = 0;
        random = new Random();
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        timeElapsed += delta;

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        guardian.update(delta);

        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.SPACE)) {
            try {
                float bulletWidth = Bullet.getBulletWidth(0.5f); // Lấy chiều rộng thực tế
                bullets.add(new Bullet(
                    guardian.getX() + (guardian.getWidth() / 2) - (bulletWidth / 2),
                    guardian.getY() + guardian.getHeight(),
                    0.5f,
                    500
                ));
            } catch (RuntimeException e) {
                Gdx.app.error("Bullet Error", e.getMessage());
            }
        }

        spawnTimer += delta;
        if (spawnTimer >= 1.5f) {
            float enemyX = random.nextFloat() * (Gdx.graphics.getWidth() - 50);
            float enemySpeed = baseEnemySpeed + timeElapsed * speedIncrementRate;
            enemies.add(new Enemy("enemy.png", enemyX, Gdx.graphics.getHeight(), 0.5f, enemySpeed));
            spawnTimer = 0;
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

        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            bullet.update(delta);
            if (bullet.isOffScreen(Gdx.graphics.getHeight())) {
                bullets.remove(i);
                i--;
            } else {
                bullet.draw(batch);
            }
        }

        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            for (int j = 0; j < enemies.size(); j++) {
                Enemy enemy = enemies.get(j);
                if (enemy.isColliding(bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight())) {
                    bullets.remove(i);
                    enemies.remove(j);
                    i--;
                    break;
                }
            }
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        guardian.dispose();
        for (Enemy enemy : enemies) {
            enemy.dispose();
        }
        for (Bullet bullet : bullets) {
            bullet.dispose();
        }
        backgroundTexture.dispose();
    }
}
