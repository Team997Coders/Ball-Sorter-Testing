package frc.robot.commands;

import org.junit.*;
import static org.junit.Assert.*;
import frc.robot.commands.*;
import static org.mockito.Mockito.*;
import frc.robot.subsystems.*;

public class SortstuffUnitTest {

    Sortstuff sortstuff = null;
    Sorter sorterMock = null;

    @Before
    public void init() {
        sorterMock = mock(Sorter.class);
        sortstuff = new Sortstuff(true, sorterMock);
        sortstuff.initialize();
    }

    @Test
    public void doesThisRunEvery500ms() throws InterruptedException {
        int counter = 0;
        while (counter < 500) {
            Thread.sleep(10);
            sortstuff.execute();
            counter++;
        }
        verify(sorterMock, times(10)).manageQueue();
    }
}