package io.github.fakebrotato;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Enemy {
    private Texture texture;
    private float x, y;
    private float width, height;
    private float speed;

    public Enemy(String texturePath, float startX, float startY, float scaleFactor, float speed) {
        this.texture = new Texture(texturePath);
        this.x = startX;
        this.y = startY;
        this.speed = speed;

        // Tính toán kích thước Enemy
        this.width = texture.getWidth() * scaleFactor;
        this.height = texture.getHeight() * scaleFactor;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }

    public void update(float deltaTime) {
        y -= speed * deltaTime;

        // Reset vị trí nếu kẻ địch ra khỏi màn hình
        if (y + height < 0) {
            resetPosition();
        }
    }

    public void resetPosition() {
        y = Gdx.graphics.getHeight();
        x = (float) Math.random() * (Gdx.graphics.getWidth() - width);
    }

    public void dispose() {
        texture.dispose();
    }

    // Phương thức kiểm tra va chạm
    public boolean isColliding(float otherX, float otherY, float otherWidth, float otherHeight) {
        return x < otherX + otherWidth && x + width > otherX &&
            y < otherY + otherHeight && y + height > otherY;
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
