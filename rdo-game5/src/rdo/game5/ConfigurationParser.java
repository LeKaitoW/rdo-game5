package rdo.game5;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ConfigurationParser {

	public static void parseConfig(IFile configIFile) {
		JSONParser parser = new JSONParser();
		try {
			JSONObject object = (JSONObject) parser.parse(new FileReader(
					ResourcesPlugin.getWorkspace().getRoot().getLocation()
							.append(configIFile.getFullPath()).toString()));
			OutputStream outputStream = new FileOutputStream(configIFile
					.getLocation().removeLastSegments(1).append("/game5.rao")
					.toString(), true);
			PrintStream printStream = new PrintStream(outputStream, true,
					StandardCharsets.UTF_8.name());
			JSONArray places = (JSONArray) object.get("places");
			printStream
					.print(String.format(
							"resource Фишка1 = Фишка.create(1, %1$s);\n"
									+ "resource Фишка2 = Фишка.create(2, %2$s);\n"
									+ "resource Фишка3 = Фишка.create(3, %3$s);\n"
									+ "resource Фишка4 = Фишка.create(4, %4$s);\n"
									+ "resource Фишка5 = Фишка.create(5, %5$s);\n"
									+ "resource Дырка = Дырка_t.create(%6$s);\n"
									+ "\n"
									+ "search Расстановка_фишек {\n"
									+ "	set init() {\n"
									+ "		setCondition(exist(Фишка: Фишка.Номер != Фишка.Местоположение));\n"
									+ "		setTerminateCondition(forAll(Фишка: Фишка.Номер == Фишка.Местоположение));\n"
									+ "		compareTops(%7$s);\n"
									+ "		evaluateBy(%8$s);\n"
									+ "	}\n"
									+ "	activity Перемещение_вправо checks Перемещение_фишки(Место_дырки.справа,  1).setValue%9$s(%10$s);\n"
									+ "	activity Перемещение_влево checks Перемещение_фишки(Место_дырки.слева,  -1).setValue%11$s(%12$s);\n"
									+ "	activity Перемещение_вверх checks Перемещение_фишки(Место_дырки.сверху,  -3).setValue%13$s(%14$s);\n"
									+ "	activity Перемещение_вниз checks Перемещение_фишки(Место_дырки.снизу,  3).setValue%15$s(%16$s);\n"
									+ "}\n", places.get(0), places.get(1),
							places.get(2), places.get(3), places.get(4),
							places.get(5), object.get("compare"),
							object.get("heuristic"),
							object.get("computeRight"),
							object.get("costRight"), object.get("computeLeft"),
							object.get("costLeft"), object.get("computeUp"),
							object.get("costUp"), object.get("computeDown"),
							object.get("costDown")));
			if (!object.get("code").equals("")) {
				printStream.println(String.format("\n" + "%1$s",
						object.get("code")));
			}
			printStream.close();

		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
}