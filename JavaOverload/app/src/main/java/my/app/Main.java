package my.app;

import com.lib1.Obj1;
import net.library.Helper;

public class Main {
	public static void main(String[] args) {
		Obj1 o = new Obj1();
		Helper h = new Helper();
		String s = h.which(o);
		System.out.println(s);
	}
}
