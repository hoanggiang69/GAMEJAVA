package io.github.fakebrotato;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;

public class Boss {
    private Texture texture;
    private float x, y;
    private float speed;
    private float width, height;
    private float maxHealth, currentHealth;
    private float direction = 1;
    private float shootTimer = 0;
    private int shootCount = 0;
    private boolean isReloading = false;
    private float reloadTimer = 0;

    public Boss(float startX, float startY, float scaleFactor, float speed, float initialHealth) {
        this.texture = new Texture("boss.jpg");
        this.x = startX;
        this.y = startY;
        this.speed = speed;
        this.width = texture.getWidth() * scaleFactor;
        this.height = texture.getHeight() * scaleFactor;
        this.maxHealth = initialHealth;
        this.currentHealth = maxHealth;
    }

    public void update(float deltaTime, ArrayList<Bullet> bossBullets) {
        x += speed * direction * deltaTime;
        if (x < 0) {
            x = 0;
            direction = 1;
        } else if (x > Gdx.graphics.getWidth() - width) {
            x = Gdx.graphics.getWidth() - width;
            direction = -1;
        }

        if (!isReloading) {
            shootTimer += deltaTime;
            if (shootTimer >= 1f) {
                float wingOffset = width / 4;
                // Đạn của boss xoay 180 độ
                bossBullets.add(new Bullet(x + wingOffset - Bullet.getBulletWidth(0.5f) / 2, y, 0.5f, -300, 180));
                bossBullets.add(new Bullet(x + width - wingOffset - Bullet.getBulletWidth(0.5f) / 2, y, 0.5f, -300, 180));
                shootTimer = 0;
                shootCount++;
                if (shootCount >= 5) {
                    isReloading = true;
                    reloadTimer = 0;
                    shootCount = 0;
                }
            }
        } else {
            reloadTimer += deltaTime;
            if (reloadTimer >= 3f) {
                isReloading = false;
            }
        }
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }

    public void drawHealthBar(ShapeRenderer shapeRenderer) {
        float healthBarWidth = width;
        float healthBarHeight = 10;
        float healthRatio = currentHealth / maxHealth;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.rect(x, y - healthBarHeight - 10, healthBarWidth, healthBarHeight);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(x, y - healthBarHeight - 10, healthBarWidth * healthRatio, healthBarHeight);
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

    public float getHealth() {
        return currentHealth;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void dispose() {
        texture.dispose();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
