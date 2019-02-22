package com.adamdabrowski.game;

import com.adamdabrowski.server.ClassConfig;
import com.adamdabrowski.server.IConfig;
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
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Game extends ApplicationAdapter {
//	Stage stage;
//	TextArea textArea;
	SpriteBatch batch;
	BitmapFont font;
	ChatInput chatInput;
//	Texture img;

	Queue<String> messages;
	Logic logic;

	@Override
	public void create () {
//		stage = new Stage(new ScreenViewport());
//		Gdx.input.setInputProcessor(stage);

//		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
//		textArea = new TextArea("test", style);
//		textArea.setPosition(0, 0);
//		stage.addActor(textArea);

		messages = new LinkedBlockingQueue<String>();
		logic = Logic.getInstance();

		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		chatInput = new ChatInput(messages);
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
		batch.end();

		// Input
		if (!logic.isChatOpen && Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
			logic.OpenChat();
			Gdx.input.getTextInput(chatInput, "Send chat message", "Hello!", "...");
		}
	}
	
	@Override
	public void dispose () {
//		stage.dispose();
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


			Thread incoming = new Thread(new ClientIncoming(inputStream));
			Thread outgoing = new Thread(new ClientOutgoing(outputStream, messages));

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
