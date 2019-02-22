package com.adamdabrowski.game;

import com.adamdabrowski.server.ClassConfig;
import com.adamdabrowski.server.IConfig;
import com.adamdabrowski.server.Message;
import com.adamdabrowski.server.MessageType;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Game extends ApplicationAdapter {
	SpriteBatch batch;
	ShapeRenderer shapeRenderer;
	BitmapFont font;
	ChatInput chatInput;
//	Texture img;

	Logic logic;
	ActionQueue actionQueue;

	@Override
	public void create () {
		logic = Logic.getInstance();
		actionQueue = ActionQueue.getInstance();

		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		chatInput = new ChatInput();
//		img = new Texture("badlogic.jpg");

		Gdx.input.setInputProcessor(new GameInput(logic, actionQueue, chatInput));

		Connect();
	}

	public void resize (int width, int height) {
//		stage.getViewport().update(width, height, true);
	}

	@Override
	public void render () {
		float deltaTime = Gdx.graphics.getDeltaTime();
//		logic.Update(deltaTime);

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Ground
		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		shapeRenderer.circle(w / 2, h / 2, logic.gameState.radius);
		shapeRenderer.end();

		batch.begin();
		// Chat history
		font.draw(batch, logic.GetMessages(), w - 200 - 10, h - 10, 200, Align.left, true);
		// Lobby status
		String lobbyMsg = logic.isInLobby ? "Will join next game" : "Will not join next game";
		font.draw(batch, lobbyMsg, w - 200 - 10, 30, 200, Align.center, false);
		batch.end();


	}
	
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
//		img.dispose();
	}

	private void Connect() {
		IConfig config = new ClassConfig();

		Socket client;
		ObjectOutputStream outputStream;
		ObjectInputStream inputStream;
		try {
			client = new Socket(config.GetHost(), config.GetPort());
			outputStream = new ObjectOutputStream(client.getOutputStream());
			inputStream = new ObjectInputStream(client.getInputStream());


			Thread incoming = new Thread(new ClientIncoming(inputStream, logic));
			Thread outgoing = new Thread(new ClientOutgoing(outputStream, actionQueue.GetActionQueue()));

			incoming.start();
			outgoing.start();

//			outgoing.join();
//			incoming.interrupt();

//			inputStream.close();
//			outputStream.close();
//			client.close();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
