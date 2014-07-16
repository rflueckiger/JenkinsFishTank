package ch.nickthegreek.jenkins.fishtank.util;

import ch.nickthegreek.jenkins.fishtank.FishState;
import ch.nickthegreek.jenkins.fishtank.JsonDataSource;
import com.sun.tools.internal.jxc.SchemaGenerator;

import java.io.*;

/**
 * <p>Loads fish events from a script file. The file has one event per line, events need not to be ordered. A line must
 * have the following format:</p>
 *
 * <pre>
 *      time : fishName -> state
 * </pre>
 *
 * <ul>
 *     <li>time: how many seconds after the script was started the event should occur.</li>
 *     <li>fishName: the name of the fish affected by this event.</li>
 *     <li>state: the state the fish should receive at this time. see {@link FishState}</li>
 * </ul>
 *
 * <pre>
 *     5 : FancyFish -> DEAD
 *     10 : FancyFish -> GHOST
 * </pre>
 */
public class FishEventScriptLoader {

    private final File file;

    public FishEventScriptLoader(File file) {
        if (file == null) {
            throw new IllegalArgumentException("file must not be null.");
        }
        this.file = file;
    }

    public FishEventScript getFishEventScript() {
        FishEventScript script = new FishEventScript();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            String line = null;
            while ((line = reader.readLine()) != null) {
                long time;
                String name;
                FishState state;

                String[] timeAndEvent = line.split(":");
                if (timeAndEvent != null && timeAndEvent.length == 2) {
                    time = Long.parseLong(timeAndEvent[0].trim());

                    String[] nameAndState = timeAndEvent[1].trim().split("->");
                    if (nameAndState != null && nameAndState.length == 2) {
                        name = nameAndState[0].trim();
                        state = FishState.valueOf(nameAndState[1].trim());
                    } else {
                        // TODO: log invalid event syntax
                        continue;
                    }
                } else {
                    // TODO: log invalid event syntax
                    continue;
                }

                script.add(time, name, state);
            }

            return script;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
