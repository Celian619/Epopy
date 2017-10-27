package net.epopy.network;

import net.epopy.network.games.modules.Location3D;

public class Test implements Runnable {


	public static void main(String[] args) {
		new Test();
	}

	Location3D loc = new Location3D(0, 0, 0, 0, 0);

	public Test() {
		//	List<Location3D> que = new ArrayList<>();
		//que.add(new Location3D(10, 10, 0, 0, 10));
		//que.add(new Location3D(12, 10, 0, 0, 10));

		Location3D to = new Location3D(10, 2, 0, 0, 10);


		//for(int i = 2; i) {
			double x = (loc.getX() + to.getX())/2;
			double y = (loc.getY() + to.getY())/2;

			Location3D milieu = new Location3D(x, y, 0, -1, loc.getDirection());
			System.out.println(milieu.getX() + "  " + milieu.getY());
	//	}
	}

	@Override
	public void run() {
		System.out.println("update");
	}
}
