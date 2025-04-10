package io.github.fakebrotato;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;

public class Bomber {
    private Texture texture;
    private float x, y;
    private float speed = 500; // Tốc độ cố định
    private float width, height;
    private float maxHealth = 3;
    private float currentHealth = maxHealth;
    private float shootTimer = 0;
    private boolean fromLeft; // Hướng di chuyển: true (trái sang phải), false (phải sang trái)
    private float rotation; // Góc xoay của texture

    public Bomber(float startX, float startY, boolean fromLeft) {
        this.texture = new Texture("bomber.png");
        this.x = startX;
        this.y = startY;
        this.fromLeft = fromLeft;
        this.rotation = fromLeft ? -90 : 90; // Sửa góc xoay: trái sang phải (-90), phải sang trái (90)
        this.speed = fromLeft ? speed : -speed; // Tốc độ dương hoặc âm tùy hướng
        this.width = texture.getWidth() * 0.5f;
        this.height = texture.getHeight() * 0.5f;
    }

    public void update(float deltaTime, ArrayList<Bullet> bossBullets) {
        x += speed * deltaTime;

        shootTimer += deltaTime;
        if (shootTimer >= 1f) {
            bossBullets.add(new Bullet(x + width / 2 - Bullet.getBulletWidth(0.5f) / 2, y, 0.5f, -200, 0, "bomb.png"));
            shootTimer = 0;
        }
    }

    public void draw(SpriteBatch batch) {
        batch.draw(
            texture,
            x, y,
            width / 2, height / 2,
            width, height,
            1, 1,
            rotation,
            0, 0,
            texture.getWidth(), texture.getHeight(),
            false, false
        );
    }

    public void drawHealthBar(ShapeRenderer shapeRenderer) {
        float healthBarWidth = width;
        float healthBarHeight = 10;
        float healthRatio = currentHealth / maxHealth;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect(x, y + height + 10, healthBarWidth, healthBarHeight);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(x, y + height + 10, healthBarWidth * healthRatio, healthBarHeight);
        shapeRenderer.end();
    }

    public boolean isColliding(float otherX, float otherY, float otherWidth, float otherHeight) {
        return otherX < x + width &&
            otherX + otherWidth > x &&
            otherY < y + height &&
            otherY + otherHeight > y;
    }

    public void takeDamage(float damage) {
        currentHealth -= damage;
        if (currentHealth < 0) currentHealth = 0;
    }

    public boolean isDead() {
        return currentHealth <= 0;
    }

    public boolean isOffScreen() {
        return (fromLeft && x > Gdx.graphics.getWidth()) || (!fromLeft && x < -width);
    }

    public void dispose() {
        texture.dispose();
    }
}
