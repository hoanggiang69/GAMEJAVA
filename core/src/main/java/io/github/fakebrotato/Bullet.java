package io.github.fakebrotato;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bullet {
    private Texture texture;
    private float x, y;
    private float speed;
    private float width, height;

    public Bullet(float startX, float startY, float scaleFactor, float speed) {
        this.texture = new Texture("bullet.png"); // Không xử lý lỗi, nếu file không tồn tại sẽ crash
        this.width = texture.getWidth() * scaleFactor; // Tính trước width
        this.height = texture.getHeight() * scaleFactor; // Tính trước height
        this.x = startX; // Gán x sau khi width đã được tính
        this.y = startY;
        this.speed = speed;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }

    public void update(float deltaTime) {
        y += speed * deltaTime;
    }

    public boolean isOffScreen(float screenHeight) {
        return y > screenHeight;
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

    // Phương thức tĩnh để lấy chiều rộng của đạn trước khi tạo
    public static float getBulletWidth(float scaleFactor) {
        Texture tempTexture = new Texture("bullet.png");
        float width = tempTexture.getWidth() * scaleFactor;
        tempTexture.dispose(); // Giải phóng texture tạm thời
        return width;
    }
}
