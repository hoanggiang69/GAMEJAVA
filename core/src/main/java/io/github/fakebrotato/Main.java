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
    private float spawnTimer; // Bộ đếm thời gian để spawn kẻ địch
    private float timeElapsed; // Thời gian chơi
    private Random random;

    private float baseEnemySpeed = 150; // Tốc độ ban đầu của kẻ địch
    private float speedIncrementRate = 10; // Tốc độ tăng thêm mỗi giây

    @Override
    public void create() {
        batch = new SpriteBatch();

        // Tạo Guardian
        guardian = new Guardian("guardian.png",
            Gdx.graphics.getWidth() / 2f - 50,
            100,
            0.5f);

        // Tạo danh sách kẻ địch và đạn
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();

        // Tải hình nền (định dạng jpg)
        backgroundTexture = new Texture("background.jpg");

        // Khởi tạo bộ đếm thời gian và Random
        spawnTimer = 0;
        timeElapsed = 0;
        random = new Random();
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // Cập nhật thời gian chơi
        timeElapsed += delta;

        // Xóa màn hình
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Cập nhật Guardian
        guardian.update(delta);

        // Kiểm tra input để bắn đạn
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.SPACE)) {
            bullets.add(new Bullet(
                guardian.getX() + guardian.getWidth() / 2 - 5, // Vị trí giữa Guardian
                guardian.getY() + guardian.getHeight(),
                0.5f,
                500 // Tốc độ đạn
            ));
        }

        // Spawn kẻ địch
        spawnTimer += delta;
        if (spawnTimer >= 1.5f) { // Tạo kẻ địch mỗi 1.5 giây
            float enemyX = random.nextFloat() * (Gdx.graphics.getWidth() - 50); // Vị trí ngẫu nhiên trên trục X
            float enemySpeed = baseEnemySpeed + timeElapsed * speedIncrementRate; // Tốc độ tăng dần
            enemies.add(new Enemy("enemy.png", enemyX, Gdx.graphics.getHeight(), 0.5f, enemySpeed));
            spawnTimer = 0;
        }

        // Cập nhật và vẽ
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Vẽ Guardian
        guardian.draw(batch);

        // Cập nhật và vẽ kẻ địch
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            enemy.update(delta);

            // Xóa kẻ địch nếu ra khỏi màn hình
            if (enemy.getY() < -enemy.getHeight()) {
                enemies.remove(i);
                i--;
            } else {
                enemy.draw(batch);
            }
        }

        // Cập nhật và vẽ đạn
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            bullet.update(delta);

            // Xóa đạn nếu ra khỏi màn hình
            if (bullet.isOffScreen(Gdx.graphics.getHeight())) {
                bullets.remove(i);
                i--;
            } else {
                bullet.draw(batch);
            }
        }

        // Xử lý va chạm giữa đạn và kẻ địch
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);

            for (int j = 0; j < enemies.size(); j++) {
                Enemy enemy = enemies.get(j);

                if (enemy.isColliding(bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight())) {
                    // Va chạm: Xóa đạn và kẻ địch
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
