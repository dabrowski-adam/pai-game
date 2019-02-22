package com.adamdabrowski.game;

import com.adamdabrowski.server.ClassConfig;
import com.adamdabrowski.server.IConfig;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Game extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	Logic logic;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");

		logic = Logic.getInstance();

		Connect();
	}

	@Override
	public void render () {
		float deltaTime = Gdx.graphics.getDeltaTime();
		logic.Update(deltaTime);

		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
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


//			Thread incoming = new Thread(new ClientIncoming(inputStream));
//			Thread outgoing = new Thread(new ClientOutgoing(outputStream));

//			incoming.start();
//			outgoing.start();

//			outgoing.join();
//			incoming.interrupt();

			inputStream.close();
			outputStream.close();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
