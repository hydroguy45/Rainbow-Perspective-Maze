

import java.awt.Color;

public class Line implements Comparable<Line> {
	public float x1;
	public float y1;
	public float x2;
	public float y2;
	public float r1;
	// in rads
	public float pheta1;
	public float r2;
	public float pheta2;
	public Color c;

	static Color appropriateColorValue(float midX, float midY, float maxX, float maxY) {
		/*
		 * COLOR LAYOUT IN MAP
		 * 
		 * y = maxY ^ | very blue | x = 0 <--red--+--green---> x = maxX | kind
		 * of blue | V y = 0
		 */

		float blue = (midY / maxY);
		float red;
		float green;
		if (2 * midX > maxX) {
			green = (midX - (maxX / 2)) / (maxX / 2);
			red = 0;
		} else {
			red = (midX) / (maxX / 2);
			green = 0;
		}
		return new Color(red, green, blue);
	}

	private int whichQuadrant(float x, float y) {
		if (x >= 0 && y >= 0) {
			return 1;
		} else if (x < 0 && y >= 0) {
			return 2;
		} else if (x < 0 && y < 0) {
			return 3;
		} else if (x >= 0 && y < 0) {
			return 4;
		}
		System.out.print("Which Quadrant Error!");
		return 0;
	}

	private float angle(float x, float y) {
		if (y > 0) {
			return (float) Math.atan(x / y);
		} else if (y == 0) {
			return (float) Math.asin(x / Math.abs(x));
		} else {
			return (float) (Math.atan(x / y) + Math.PI);
		}
	}

	public Line(float ax1, float by1, float ax2, float by2, Color color) {
		x1 = ax1;
		y1 = by1;
		x2 = ax2;
		y2 = by2;
		c = color;
		r1 = (float) Math.sqrt(Math.pow(x1, 2) + Math.pow(y1, 2));
		pheta1 = angle(x1, y1);
		r2 = (float) Math.sqrt(Math.pow(x2, 2) + Math.pow(y2, 2));
		pheta2 = angle(x2, y2);

	}

	public void printData() {
		System.out.println("(" + Float.toString(x1) + "," + Float.toString(y1) + ")|(" + Float.toString(x2) + ","
				+ Float.toString(y2) + ")");
		System.out.println("(" + Float.toString(pheta1) + "," + Float.toString(r1) + ")|(" + Float.toString(pheta2)
				+ "," + Float.toString(r2) + ")");
	}

	public Line translateOver(float deltaX, float deltaY) {
		return new Line(x1 - deltaX, y1 - deltaY, x2 - deltaX, y2 - deltaY, c);
	}

	private Line crop(float degrees) {
		double degree1 = Math.toDegrees(pheta1);
		double degree2 = Math.toDegrees(pheta2);
		// this is because I kept running into an issue where 225 (aka -135)
		// wouldn't be cropped
		// because both if statements would pass it onto the other.
		if (degree1 > 180) {
			degree1 -= 360;
		}
		if (degree2 > 180) {
			degree2 -= 360;
		}
		if ((x1 - x2) != 0) {
			float slope = (y1 - y2) / (x1 - x2);
			if (degree1 > degrees) {
				float interceptSlope = (float) (1 / Math.tan(Math.toRadians(degrees)));
				float newX = (y2 - x2 * slope) / (interceptSlope - slope);
				float newY = slope * (newX - x2) + y2;
				x1 = newX;
				y1 = newY;
			}
			if (degree1 < -degrees) {
				float interceptSlope = (float) (-1 / Math.tan(Math.toRadians(degrees)));
				float newX = (y2 - x2 * slope) / (interceptSlope - slope);
				float newY = slope * (newX - x2) + y2;
				x1 = newX;
				y1 = newY;
			}
			if (degree2 > degrees) {
				float interceptSlope = (float) (1 / Math.tan(Math.toRadians(degrees)));
				float newX = (y2 - x2 * slope) / (interceptSlope - slope);
				float newY = slope * (newX - x2) + y2;
				x2 = newX;
				y2 = newY;
			}
			if (degree2 < -degrees) {
				float interceptSlope = (float) (-1 / Math.tan(Math.toRadians(degrees)));
				float newX = (y2 - x2 * slope) / (interceptSlope - slope);
				float newY = slope * (newX - x2) + y2;
				x2 = newX;
				y2 = newY;
			}
		} else {
			// TODO: FIX THE 225 degree error... aka refix the degree bounds so
			// 255 doesn't slip past -45
			if (degree1 > degrees) {
				float interceptSlope = (float) (1 / Math.tan(Math.toRadians(degrees)));
				y1 = interceptSlope * x1;
			}
			if (degree1 < -degrees) {
				float interceptSlope = (float) (-1 / Math.tan(Math.toRadians(degrees)));
				y1 = interceptSlope * x1;
			}
			if (degree2 > degrees) {
				float interceptSlope = (float) (1 / Math.tan(Math.toRadians(degrees)));
				y2 = interceptSlope * x2;
			}
			if (degree2 < -degrees) {
				float interceptSlope = (float) (-1 / Math.tan(Math.toRadians(degrees)));
				y2 = interceptSlope * x2;
			}
		}
		return new Line(x1, y1, x2, y2, c);
	}

	public boolean ifWithinDegrees(float degrees) {
		boolean crossesFOV = (!((x1 > -degrees / 45 * y1) && (x1 < 0)) || ((x2 > -degrees / 45 * y2) && (x2 < 0))
				|| ((x1 < degrees / 45 * y1) && (x1 > 0)) || ((x2 < degrees / 45 * y2) && (x2 > 0)))
				&& (distanceAt(0, true) != -1);
		if (((y1 > 0) || (y2 > 0))
				&& ((((x1 > -degrees / 45 * y1) && (x1 < 0)) || ((x2 > -degrees / 45 * y2) && (x2 < 0))
						|| ((x1 < degrees / 45 * y1) && (x1 > 0)) || ((x2 < degrees / 45 * y2) && (x2 > 0)))
						|| crossesFOV)) {
			return true;
		} else {
			return false;
		}
	}

	public Line withinDegrees(float degrees) {
		boolean crossesFOV = (!((x1 > -degrees / 45 * y1) && (x1 < 0)) || ((x2 > -degrees / 45 * y2) && (x2 < 0))
				|| ((x1 < degrees / 45 * y1) && (x1 > 0)) || ((x2 < degrees / 45 * y2) && (x2 > 0)))
				&& (distanceAt(0, true) != -1);
		if (((y1 > 0) || (y2 > 0))
				&& ((((x1 > -degrees / 45 * y1) && (x1 < 0)) || ((x2 > -degrees / 45 * y2) && (x2 < 0))
						|| ((x1 < degrees / 45 * y1) && (x1 > 0)) || ((x2 < degrees / 45 * y2) && (x2 > 0)))
						|| crossesFOV)) {
			// TODO: fix this a little more
			return crop(degrees);
		} else {
			return null;
		}
	}

	public Line rotate(float degrees) {
		float a1 = (float) (x1 * Math.cos(Math.toRadians(degrees)) - y1 * Math.sin(Math.toRadians(degrees)));
		float b1 = (float) (x1 * Math.sin(Math.toRadians(degrees)) + y1 * Math.cos(Math.toRadians(degrees)));
		float a2 = (float) (x2 * Math.cos(Math.toRadians(degrees)) - y2 * Math.sin(Math.toRadians(degrees)));
		float b2 = (float) (x2 * Math.sin(Math.toRadians(degrees)) + y2 * Math.cos(Math.toRadians(degrees)));
		Line newLine = new Line(a1, b1, a2, b2, c);
		return newLine;
	}

	public float distanceAt(float degrees, boolean filtering) {
		// returns negative if the line doesn't contain the degree
		// DON'T USE THIS ON AN UNSEEN LINE!
		float angle1 = Math.max(pheta1, pheta2);
		float angle2 = Math.min(pheta1, pheta2);
		if ((x1 * x2 > 0) && filtering) {
			return -1; // I ran into a cropping error, basically this stops
						// horizontal lines from getting through.
		}
		float rads = (float) Math.toRadians(degrees);
		if ((rads <= angle1) && (rads >= angle2)) {
			// return r at pheta
			// finding the intersection between the degree line and the wall
			// y=m*(x-a)+ c ==> y=slope*(x-x1)+y1
			// y=a*(x-d) + e ==> y=degrees/45*(x)
			// TODO:FIX THIS the intercept slope needs to be proper
			double interceptSlope = 1 / Math.tan(rads);
			if ((x1 - x2) == 0) {
				float newX = x1;
				float newY = (float) (interceptSlope * newX);
				return (float) Math.sqrt(Math.pow(newX, 2) + Math.pow(newY, 2));
			} else {
				double slope = (y1 - y2) / (x1 - x2);
				if (slope != interceptSlope) {
					float newX = (float) ((y2 - x2 * slope) / (interceptSlope - slope));
					float newY = (float) (slope * (newX - x2) + y2);
					return (float) Math.sqrt(Math.pow(newX, 2) + Math.pow(newY, 2));
				} else {
					return Math.min(r1, r2);
				}
			}
		} else {
			return -1;
		}
	}

	@Override
	public int compareTo(Line arg0) {
		if (arg0.equals(this) || ((x1 == arg0.x2) && (x2 == arg0.x1) && (y1 == arg0.y2) && (y2 == arg0.y1))) {
			return 0;
		} else if (x1 > arg0.x1) {
			return 1;
		} else if (x1 < arg0.x1) {
			return -1;
		}
		return 1; // only issue occurs if 0 so this is alright
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((c == null) ? 0 : c.hashCode());
		result = prime * result + Float.floatToIntBits(x1);
		result = prime * result + Float.floatToIntBits(x2);
		result = prime * result + Float.floatToIntBits(y1);
		result = prime * result + Float.floatToIntBits(y2);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Line other = (Line) obj;
		if (c == null) {
			if (other.c != null)
				return false;
		} else if (!c.equals(other.c))
			return false;
		if (Float.floatToIntBits(x1) != Float.floatToIntBits(other.x1))
			return false;
		if (Float.floatToIntBits(x2) != Float.floatToIntBits(other.x2))
			return false;
		if (Float.floatToIntBits(y1) != Float.floatToIntBits(other.y1))
			return false;
		if (Float.floatToIntBits(y2) != Float.floatToIntBits(other.y2))
			return false;
		return true;
	}
}
