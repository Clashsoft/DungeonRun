package com.clashsoft.dungeonrun.util;

public class DimensionHelper
{
	public static class Pos1<T>
	{
		public T	x;
		
		public Pos1(T i1)
		{
			x = i1;
		}
		
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((this.x == null) ? 0 : this.x.hashCode());
			return result;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Pos1 other = (Pos1) obj;
			if (this.x == null)
			{
				if (other.x != null)
					return false;
			}
			else if (!this.x.equals(other.x))
				return false;
			return true;
		}
	}
	
	public static class Pos2<T> extends Pos1<T>
	{
		public T	y;
		
		public Pos2(T i1, T i2)
		{
			super(i1);
			y = i2;
		}
		
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((this.y == null) ? 0 : this.y.hashCode());
			return result;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			Pos2 other = (Pos2) obj;
			if (this.y == null)
			{
				if (other.y != null)
					return false;
			}
			else if (!this.y.equals(other.y))
				return false;
			return true;
		}
	}
	
	public static class Pos3<T> extends Pos2<T>
	{
		public T	z;
		
		public Pos3(T i1, T i2, T i3)
		{
			super(i1, i2);
			z = i3;
		}
		
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((this.z == null) ? 0 : this.z.hashCode());
			return result;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			Pos3 other = (Pos3) obj;
			if (this.z == null)
			{
				if (other.z != null)
					return false;
			}
			else if (!this.z.equals(other.z))
				return false;
			return true;
		}
	}
	
	public static class Size1<T>
	{
		public T	width;
		
		public Size1(T i1)
		{
			width = i1;
		}
		
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((this.width == null) ? 0 : this.width.hashCode());
			return result;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Size1 other = (Size1) obj;
			if (this.width == null)
			{
				if (other.width != null)
					return false;
			}
			else if (!this.width.equals(other.width))
				return false;
			return true;
		}
	}
	
	public static class Size2<T> extends Size1<T>
	{
		public T	heigth;
		
		public Size2(T i1, T i2)
		{
			super(i1);
			heigth = i2;
		}
		
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((this.heigth == null) ? 0 : this.heigth.hashCode());
			return result;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			Size2 other = (Size2) obj;
			if (this.heigth == null)
			{
				if (other.heigth != null)
					return false;
			}
			else if (!this.heigth.equals(other.heigth))
				return false;
			return true;
		}
	}
	
	public static class Size3<T> extends Size2<T>
	{
		public T	length;
		
		public Size3(T i1, T i2, T i3)
		{
			super(i1, i2);
			length = i3;
		}
		
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + ((this.length == null) ? 0 : this.length.hashCode());
			return result;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			Size3 other = (Size3) obj;
			if (this.length == null)
			{
				if (other.length != null)
					return false;
			}
			else if (!this.length.equals(other.length))
				return false;
			return true;
		}
	}
	
}
