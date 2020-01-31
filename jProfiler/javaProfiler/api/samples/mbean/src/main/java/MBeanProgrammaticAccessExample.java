import com.jprofiler.api.agent.mbean.RemoteControllerMBean;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// Shows how to connect to the JProfiler MBean programatically
// The profiled process has to be in offline profiling mode and the JMX server
// has to be started by passing -Djprofiler.jmxServerPort=[port] to the profiled JVM.
// This will not work in nowait mode because the MBean is not registered in that case.
public class MBeanProgrammaticAccessExample {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Specify the port as an argument that was passed to the profiled JVM " +
                    "with the VM parameter -Djprofiler.jmxServerPort=[port]");
        }

        String port = args[0];
        // In this case the connection is made to a process on localhost, but it could be on a remote system as well.
        // Note that the connection is made via JMX which does not work well with firewalls
        System.out.println("Connecting to localhost:" + port);
        JMXServiceURL jmxUrl = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:" + port + "/jmxrmi");
        JMXConnector connector = JMXConnectorFactory.newJMXConnector(jmxUrl, Collections.emptyMap());

        Map<String, Object> env = new HashMap<>();

        // If you have protected the JMX server with a JMX password file by passing -Djprofiler.jmxPasswordFile=[file]
        // to the profiled JVM, you can specify the password like this:
        //env.put(JMXConnector.CREDENTIALS, new String[] {"username", "password"});

        connector.connect(env);
        MBeanServerConnection connection = connector.getMBeanServerConnection();
        ObjectName objectName = new ObjectName("com.jprofiler.api.agent.mbean:type=RemoteController");
        if (!connection.isRegistered(objectName)) {
            throw new RuntimeException("JProfiler MBean not found.");
        }

        RemoteControllerMBean mbeanProxy = JMX.newMBeanProxy(connection, objectName, RemoteControllerMBean.class, true);

        // You can look up all available operations in the javadoc of com.jprofiler.api.agent.mbean.RemoteControllerMBean
        System.out.println("Recording CPU data for 5 seconds ....");
        mbeanProxy.startCPURecording(true);
        // If you do not want a dependency on the JProfiler classes (RemoteControllerMBean),
        // you can make the above call like this:
        //connection.invoke(objectName, "startCPURecording", new Object[] {true}, new String[] {Boolean.TYPE.getName()});

        Thread.sleep(5000);
        System.out.println("Saving snapshot to the working directory of the profiled JVM ....");
        mbeanProxy.saveSnapshot("jprofiler_snapshot.jps");

        connector.close();
        System.out.println("Success");

    }
}
