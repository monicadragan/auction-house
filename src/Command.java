/**
 * interfata folosita pentru executia comenzilor primite de la useri
 * @author silvia
 *
 */
public interface Command {

	void execute(int tableRow, int tableCol, TableView userPanel);
	
}
