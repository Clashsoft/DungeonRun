package com.clashsoft.dungeonrun.util;

public class DimensionHelper
{
	public static class Pos1<T>
	{
		public T x;

		public Pos1(T i1)
		{
			this.x = i1;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + (this.x == null ? 0 : this.x.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
			{
				return true;
			}
			if (obj == null)
			{
				return false;
			}
			if (this.getClass() != obj.getClass())
			{
				return false;
			}
			Pos1 other = (Pos1) obj;
			if (this.x == null)
			{
				return other.x == null;
			}
			else
				return this.x.equals(other.x);
		}
	}

	public static class Pos2<T> extends Pos1<T>
	{
		public T y;

		public Pos2(T i1, T i2)
		{
			super(i1);
			this.y = i2;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + (this.y == null ? 0 : this.y.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
			{
				return true;
			}
			if (!super.equals(obj))
			{
				return false;
			}
			if (this.getClass() != obj.getClass())
			{
				return false;
			}
			Pos2 other = (Pos2) obj;
			if (this.y == null)
			{
				return other.y == null;
			}
			else
				return this.y.equals(other.y);
		}
	}

	public static class Pos3<T> extends Pos2<T>
	{
		public T z;

		public Pos3(T i1, T i2, T i3)
		{
			super(i1, i2);
			this.z = i3;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + (this.z == null ? 0 : this.z.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
			{
				return true;
			}
			if (!super.equals(obj))
			{
				return false;
			}
			if (this.getClass() != obj.getClass())
			{
				return false;
			}
			Pos3 other = (Pos3) obj;
			if (this.z == null)
			{
				return other.z == null;
			}
			else
				return this.z.equals(other.z);
		}
	}

	public static class Size1<T>
	{
		public T width;

		public Size1(T i1)
		{
			this.width = i1;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + (this.width == null ? 0 : this.width.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
			{
				return true;
			}
			if (obj == null)
			{
				return false;
			}
			if (this.getClass() != obj.getClass())
			{
				return false;
			}
			Size1 other = (Size1) obj;
			if (this.width == null)
			{
				return other.width == null;
			}
			else
				return this.width.equals(other.width);
		}
	}

	public static class Size2<T> extends Size1<T>
	{
		public T heigth;

		public Size2(T i1, T i2)
		{
			super(i1);
			this.heigth = i2;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + (this.heigth == null ? 0 : this.heigth.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
			{
				return true;
			}
			if (!super.equals(obj))
			{
				return false;
			}
			if (this.getClass() != obj.getClass())
			{
				return false;
			}
			Size2 other = (Size2) obj;
			if (this.heigth == null)
			{
				return other.heigth == null;
			}
			else
				return this.heigth.equals(other.heigth);
		}
	}

	public static class Size3<T> extends Size2<T>
	{
		public T length;

		public Size3(T i1, T i2, T i3)
		{
			super(i1, i2);
			this.length = i3;
		}

		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + (this.length == null ? 0 : this.length.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
			{
				return true;
			}
			if (!super.equals(obj))
			{
				return false;
			}
			if (this.getClass() != obj.getClass())
			{
				return false;
			}
			Size3 other = (Size3) obj;
			if (this.length == null)
			{
				return other.length == null;
			}
			else
				return this.length.equals(other.length);
		}
	}
}
