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
import com.badlogic.gdx.utils.Align;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Game extends ApplicationAdapter {
	SpriteBatch batch;
	BitmapFont font;
	ChatInput chatInput;
//	Texture img;

	Logic logic;
	ActionQueue actionQueue;

	@Override
	public void create () {
		logic = Logic.getInstance();
		actionQueue = ActionQueue.getInstance();

		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		chatInput = new ChatInput();
//		img = new Texture("badlogic.jpg");

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

		batch.begin();
		font.draw(batch, logic.GetMessages(), w - 200 - 10, h - 10, 200, Align.left, true);

		String lobbyMsg = logic.isInLobby ? "Will join next game" : "Will not join next game";
		font.draw(batch, lobbyMsg, w - 200 - 10, 30, 200, Align.center, false);
		batch.end();

		// Chat input
		if (!logic.isChatOpen && Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
			logic.OpenChat();

			if (!logic.isLoggedIn) {
				Gdx.input.getTextInput(chatInput, "Enter nickname", "John Smith", "...");
			} else {
				Gdx.input.getTextInput(chatInput, "Send chat message", "Hello!", "...");
			}
		}

		// Lobby toggle
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			if (logic.isInLobby) {
				actionQueue.QueueAction(new Message(MessageType.LOBBY_LEAVE, ""));
			} else {
				actionQueue.QueueAction(new Message(MessageType.LOBBY_JOIN, ""));
			}
		}
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
