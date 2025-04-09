package io.github.fakebrotato;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Guardian {
    private Texture texture;
    private float x, y;
    private float width, height;
    private float speed = 950; // tốc độ di chuyển (pixel mỗi giây)

    public Guardian(String texturePath, float startX, float startY, float scaleFactor) {
        this.texture = new Texture(texturePath);
        this.x = startX;
        this.y = startY;

        // Tính toán kích thước Guardian
        this.width = texture.getWidth() * scaleFactor;
        this.height = texture.getHeight() * scaleFactor;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }

    public void update(float deltaTime) {
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT)) x -= speed * deltaTime;
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT)) x += speed * deltaTime;
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.UP)) y += speed * deltaTime;
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.DOWN)) y -= speed * deltaTime;

        // Giới hạn trong màn hình
        x = Math.max(0, Math.min(x, Gdx.graphics.getWidth() - width));
        y = Math.max(0, Math.min(y, Gdx.graphics.getHeight() - height));
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
