package com.tahakorkem.snakex;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class GameScreen implements Screen {

    private static final float SNAKE_SPEED = 6;

    private static final int WIDTH = 760;
    private static final int HEIGHT = 760;

    private final int TILE_COUNT_PER_ROW = 19;
    private final int TILE_COUNT_PER_COLUMN = 19;

    private final int GRID_WIDTH = WIDTH / TILE_COUNT_PER_ROW;
    private final int GRID_HEIGHT = HEIGHT / TILE_COUNT_PER_COLUMN;

    private static final Color BACKGROUND_COLOR = Color.valueOf("#0c2a36");
    private static final Color GRID_TILE_COLOR = Color.valueOf("#0f3443");
    private static final Color SNAKE_HEAD_COLOR = Color.valueOf("#3db4d8");
    private static final Color SNAKE_TAIL_COLOR = Color.valueOf("#34e89e");

    private Direction direction = Direction.RIGHT;
    private int score = 0;
    private boolean gameOver = false;

    final SnakeX game;

    //private final Texture snakeImage;
    private final Array<Rectangle> snake;
    private final Rectangle apple;
    private final Texture appleImage;

    private final ShapeRenderer bgRenderer;
    private final ShapeRenderer fgRenderer;

    private final Random random;

    public GameScreen(SnakeX game) {
        this.game = game;

        random = new Random();

        Rectangle head = new Rectangle(GRID_WIDTH * 5, (HEIGHT - GRID_HEIGHT) / 2f, GRID_WIDTH, GRID_HEIGHT);
        Rectangle tail1 = new Rectangle(head.x - GRID_WIDTH, head.y, GRID_WIDTH, GRID_HEIGHT);
        Rectangle tail2 = new Rectangle(tail1.x - GRID_WIDTH, tail1.y, GRID_WIDTH, GRID_HEIGHT);
        snake = new Array<>(new Rectangle[]{head, tail1, tail2});

        apple = new Rectangle();
        apple.width = GRID_WIDTH;
        apple.height = GRID_HEIGHT;
        randomizeAppleCoord();

        appleImage = new Texture(Gdx.files.internal("apple.png"));

        bgRenderer = new ShapeRenderer();
        fgRenderer = new ShapeRenderer();
    }

    private void randomizeAppleCoord() {
        int randomRow = random.nextInt(TILE_COUNT_PER_ROW);
        int randomColumn = random.nextInt(TILE_COUNT_PER_COLUMN);
        apple.x = randomRow * GRID_WIDTH;
        apple.y = randomColumn * GRID_HEIGHT;
    }

    private float time = 0;

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (gameOver) {
            return;
        }

        ScreenUtils.clear(BACKGROUND_COLOR);

        time += delta;

        bgRenderer.begin(ShapeRenderer.ShapeType.Filled);
        bgRenderer.setColor(GRID_TILE_COLOR);
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                if ((i - j) % 2 == 0)
                    bgRenderer.rect(GRID_WIDTH * i, GRID_HEIGHT * j, GRID_WIDTH, GRID_HEIGHT);
            }
        }
        bgRenderer.end();

        fgRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // draw snake
        boolean isHead = true;
        for (Rectangle s : snake) {
            fgRenderer.setColor(isHead ? SNAKE_HEAD_COLOR : SNAKE_TAIL_COLOR);
            fgRenderer.rect(s.x, s.y, s.width, s.height);
            isHead = false;
        }

        fgRenderer.end();

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && direction != Direction.LEFT) {
            direction = Direction.RIGHT;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && direction != Direction.RIGHT) {
            direction = Direction.LEFT;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && direction != Direction.DOWN) {
            direction = Direction.UP;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && direction != Direction.UP) {
            direction = Direction.DOWN;
        }

        if (time >= 1 / SNAKE_SPEED) {
            time -= 1 / SNAKE_SPEED;

            final Rectangle lastTail = new Rectangle(snake.get(snake.size - 1));

            for (int i = snake.size - 1; i >= 0; i--) {
                Rectangle s = snake.get(i);

                if (i == 0) {
                    switch (direction) {
                        case UP:
                            snake.first().y += GRID_HEIGHT;
                            break;
                        case DOWN:
                            snake.first().y -= GRID_HEIGHT;
                            break;
                        case RIGHT:
                            snake.first().x += GRID_WIDTH;
                            break;
                        case LEFT:
                            snake.first().x -= GRID_WIDTH;
                            break;
                    }
                } else {
                    Rectangle prev = snake.get(i - 1);
                    s.set(prev);
                }
            }

            if (snake.first().overlaps(apple)) {
                snake.add(lastTail);
                randomizeAppleCoord();
                score++;
            }

            for (int i = 1; i < snake.size; i++) {
                Rectangle s = snake.get(i);
                if (snake.first().overlaps(s)) {
                    gameOver = true;
                    break;
                }
            }

            if (snake.first().x + snake.first().width > WIDTH) {
                gameOver = true;
            }
            if (snake.first().x < 0) {
                gameOver = true;
            }
            if (snake.first().y + snake.first().height > HEIGHT) {
                gameOver = true;
            }
            if (snake.first().y < 0) {
                gameOver = true;
            }

        }

        game.batch.begin();

        game.batch.draw(appleImage, apple.x, apple.y, apple.width, apple.height);

        game.font.draw(game.batch, "Score: " + score, (WIDTH - 200) / 2f, HEIGHT - 10, 200, 1, false);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    enum Direction {
        UP, DOWN, RIGHT, LEFT
    }

}
