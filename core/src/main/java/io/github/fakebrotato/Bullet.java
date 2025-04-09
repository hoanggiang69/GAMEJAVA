package io.github.fakebrotato;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bullet {
    private Texture texture;
    private float x, y;
    private float speed;
    private float width, height;

    public Bullet(float startX, float startY, float scaleFactor, float speed) {
        // Tải texture từ file bullet.png
        this.texture = new Texture("bullet.png");
        this.x = startX;
        this.y = startY;
        this.speed = speed;

        // Kích thước đạn
        this.width = texture.getWidth() * scaleFactor;
        this.height = texture.getHeight() * scaleFactor;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }

    public void update(float deltaTime) {
        y += speed * deltaTime; // Đạn di chuyển lên trên
    }

    public boolean isOffScreen(float screenHeight) {
        return y > screenHeight; // Kiểm tra nếu đạn ra khỏi màn hình
    }

    public void dispose() {
        texture.dispose();
    }

    // Getter cho tọa độ và kích thước
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
