package frc.robot.subsystems;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.DoubleSolenoid.*;
import frc.robot.subsystems.Sorter;

import org.junit.*;

import static org.mockito.Mockito.*;

public class SorterTest {
    static {
        HLUsageReporting.SetImplementation(new HLUsageReporting.Null()); // CCB: Magic bits to turn off wpi usage reporting...nonsense dependency
    }

    @Test
    public void itShouldExtendPistonWhenEjecting()
    {
        // Assemble
        // Use Mockito to mock our sorter dependencies
        // We don't want to have to wire up real hardware to test the Sorter class
        VictorSP motorMock = mock(VictorSP.class);
        DoubleSolenoid pistonMock = mock(DoubleSolenoid.class);
        DigitalOutput ballSensorMock = mock(DigitalOutput.class);
        // Wire up our class under test
        Sorter sorter = new Sorter(motorMock, pistonMock, ballSensorMock);

        // Act
        sorter.ejectBall();

        // Assert
        verify(pistonMock, times(1)).set(Value.kForward);
    }
}