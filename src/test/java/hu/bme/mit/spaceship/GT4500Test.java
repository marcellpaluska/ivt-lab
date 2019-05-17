package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class GT4500Test {

  private GT4500 ship;
  private TorpedoStore primaryTorpedoStore;
  private TorpedoStore secondaryTorpedoStore;

  @BeforeEach
  public void init(){
    primaryTorpedoStore = mock(TorpedoStore.class);
    secondaryTorpedoStore = mock(TorpedoStore.class);
    this.ship = new GT4500(primaryTorpedoStore, secondaryTorpedoStore);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange
    when(primaryTorpedoStore.fire(1)).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
  }

  @Test
  public void fireTorpedo_All_Success(){
    // Arrange
    when(primaryTorpedoStore.fire(1)).thenReturn(true);
    when(secondaryTorpedoStore.fire(1)).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, result);
  }

  @Test
  public void fireTorpedo_Single_FirePrimaryFirst() {
    ship.fireTorpedo(FiringMode.SINGLE);

    verify(primaryTorpedoStore, times(1)).fire(1);
    verify(secondaryTorpedoStore, never()).fire(anyInt());

  }

  @Test
  public void fireTorpedo_All_FireBoth() {
    ship.fireTorpedo(FiringMode.ALL);

    verify(primaryTorpedoStore, times(1)).fire(1);
    verify(secondaryTorpedoStore, times(1)).fire(1);

  }

  @Test
  public void fireTorpedo_Single_Alternating() {
    ship.fireTorpedo(FiringMode.SINGLE);

    verify(primaryTorpedoStore, times(1)).fire(1);
    verify(secondaryTorpedoStore, never()).fire(anyInt());

    ship.fireTorpedo(FiringMode.SINGLE);
    verify(primaryTorpedoStore, times(1)).fire(1);
    verify(secondaryTorpedoStore, times(1)).fire(1);

  }

  @Test
  public void fireTorpedo_Single_PrimaryEmpty() {
    when(primaryTorpedoStore.isEmpty()).thenReturn(true);

    ship.fireTorpedo(FiringMode.SINGLE);

    verify(primaryTorpedoStore, never()).fire(anyInt());
    verify(secondaryTorpedoStore, times(1)).fire(1);

  }

  @Test
  public void fireTorpedo_Single_SecondaryEmptySecondFire() {
    when(secondaryTorpedoStore.isEmpty()).thenReturn(true);

    ship.fireTorpedo(FiringMode.SINGLE);
    ship.fireTorpedo(FiringMode.SINGLE);

    verify(primaryTorpedoStore, times(2)).fire(1);
    verify(secondaryTorpedoStore, never()).fire(anyInt());

  }

  @Test
  public void fireTorpedo_Single_BothEmpty() {
    when(primaryTorpedoStore.isEmpty()).thenReturn(true);
    when(secondaryTorpedoStore.isEmpty()).thenReturn(true);

    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    verify(primaryTorpedoStore, never()).fire(anyInt());
    verify(secondaryTorpedoStore, never()).fire(anyInt());
    assertEquals(false, result);

  }

  @Test
  public void fireTorpedo_All_BothFail() {
    when(primaryTorpedoStore.fire(1)).thenReturn(false);
    when(secondaryTorpedoStore.fire(1)).thenReturn(false);

    boolean result = ship.fireTorpedo(FiringMode.ALL);

    verify(primaryTorpedoStore, times(1)).fire(1);
    verify(secondaryTorpedoStore, times(1)).fire(1);

    assertEquals(false, result);

  }

  @Test
  public void fireTorpedo_All_OneFail() {
    when(primaryTorpedoStore.fire(1)).thenReturn(false);
    when(secondaryTorpedoStore.fire(1)).thenReturn(true);

    boolean result = ship.fireTorpedo(FiringMode.ALL);

    verify(primaryTorpedoStore, times(1)).fire(1);
    verify(secondaryTorpedoStore, times(1)).fire(1);

    assertEquals(true, result);

    when(primaryTorpedoStore.fire(1)).thenReturn(true);
    when(secondaryTorpedoStore.fire(1)).thenReturn(false);

    result = ship.fireTorpedo(FiringMode.ALL);

    verify(primaryTorpedoStore, times(2)).fire(1);
    verify(secondaryTorpedoStore, times(2)).fire(1);

    assertEquals(true, result);

  }

}
