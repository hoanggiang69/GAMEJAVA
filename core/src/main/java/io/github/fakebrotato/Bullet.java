package io.github.fakebrotato;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bullet {
    private Texture texture;
    private float x, y;
    private float speed;
    private float width, height;
    private float rotation;

    public Bullet(float startX, float startY, float scaleFactor, float speed, float rotation) {
        this(startX, startY, scaleFactor, speed, rotation, "bullet.png");
    }

    public Bullet(float startX, float startY, float scaleFactor, float speed, float rotation, String texturePath) {
        this.texture = new Texture(texturePath);
        this.width = texture.getWidth() * scaleFactor;
        this.height = texture.getHeight() * scaleFactor;
        this.x = startX;
        this.y = startY;
        this.speed = speed;
        this.rotation = rotation;
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
