package io.github.fakebrotato;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bullet {
    private Texture texture;
    private float x, y;
    private float speed;
    private float width, height;
    private float rotation; // Góc xoay của đạn (độ)

    public Bullet(float startX, float startY, float scaleFactor, float speed, float rotation) {
        this.texture = new Texture("bullet.png");
        this.width = texture.getWidth() * scaleFactor;
        this.height = texture.getHeight() * scaleFactor;
        this.x = startX;
        this.y = startY;
        this.speed = speed;
        this.rotation = rotation; // Góc xoay được truyền vào constructor
    }

    public void draw(SpriteBatch batch) {
        // Sử dụng draw với rotation
        batch.draw(
            texture,
            x, y, // Vị trí
            width / 2, height / 2, // Điểm gốc (origin) để xoay, đặt ở giữa đạn
            width, height, // Kích thước
            1, 1, // Tỷ lệ scale
            rotation, // Góc xoay
            0, 0, // Vị trí nguồn trên texture
            texture.getWidth(), texture.getHeight(), // Kích thước nguồn
            false, false // Không lật texture
        );
    }

    public void update(float deltaTime) {
        y += speed * deltaTime;
    }

    public boolean isOffScreen(float screenHeight) {
        return y > screenHeight;
    }

    public boolean isColliding(float otherX, float otherY, float otherWidth, float otherHeight) {
        return x < otherX + otherWidth &&
            x + width > otherX &&
            y < otherY + otherHeight &&
            y + height > otherY;
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

    public static float getBulletWidth(float scaleFactor) {
        Texture tempTexture = new Texture("bullet.png");
        float width = tempTexture.getWidth() * scaleFactor;
        tempTexture.dispose();
        return width;
    }
}
