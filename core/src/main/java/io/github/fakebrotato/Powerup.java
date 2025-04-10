package io.github.fakebrotato;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Powerup {
    public enum Type {
        AMMO,
        LIFE
    }

    private Texture texture;
    private float x, y;
    private float speed = -100; // Tốc độ rơi xuống
    private float width, height;
    private Type type;

    public Powerup(float startX, float startY, Type type) {
        this.type = type;
        this.texture = new Texture(type == Type.AMMO ? "ammo.png" : "life.png");
        this.x = startX;
        this.y = startY;
        this.width = texture.getWidth() * 0.5f;
        this.height = texture.getHeight() * 0.5f;
    }

    public void update(float deltaTime) {
        y += speed * deltaTime;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }

    public boolean isColliding(float otherX, float otherY, float otherWidth, float otherHeight) {
        return otherX < x + width &&
            otherX + otherWidth > x &&
            otherY < y + height &&
            otherY + otherHeight > y;
    }

    public float getY() {
        return y;
    }

    public float getHeight() {
        return height;
    }

    public Type getType() {
        return type;
    }

    public void dispose() {
        texture.dispose();
    }
}
