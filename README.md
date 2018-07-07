# Mockito Tutorial

### Annotations to create mocks and mock objects

* Annotations to create mocks (@InjectMocks and @Mock)

@RunWith attaches a runner with the test class to initialize the test data
```java
@RunWith(MockitoJUnitRunner.class)
public class MathApplicationTesterWithInjectMocks { }
```

@InjectMocks annotation is used to create and inject the mock object
```java
@InjectMocks
MathApplication mathApplication = new MathApplication();
```

@Mock annotation is used to create the mock object to be injected
```java
@Mock
CalculatorService calcService;
```

For example:
```java
@RunWith(MockitoJUnitRunner.class)
public class MathApplicationTester {
	
   private MathApplication mathApplication;
   private CalculatorService calcService;

   @Before
   public void setUp(){
      mathApplication = new MathApplication();
      calcService = mock(CalculatorService.class);
      mathApplication.setCalculatorService(calcService);
   }

   @Test
   public void testAddAndSubtract(){ }
}
```

* Create mock objects: mock()

mock() creates mocks without bothering about the order of method calls that the mock is going to make in due course of its action.

```java
CalculatorService calcService = mock(CalculatorService.class);
```

For example:

```java
@RunWith(MockitoJUnitRunner.class)
public class MathApplicationTesterWithInjectMocks {
    //@InjectMocks annotation is used to create and inject the mock object
    @InjectMocks
    MathApplication mathApplication = new MathApplication();

    //@Mock annotation is used to create the mock object to be injected
    @Mock
    CalculatorService calcService;

    @Test(expected = RuntimeException.class)
    public void testAddAndSubstract(){ }
}
```

### 1) Adds a functionality to a mock: when()

Mockito adds a functionality to a mock object using the methods when().

```java
//add the behavior of calc service to add two numbers
when(calcService.add(10.0,20.0)).thenReturn(30.00);
```

### 2) Test the functionality: Assert.*method()*

Test the add() functionality

```java
Assert.assertEquals(mathApplication.add(10.0, 20.0),30.0,0);
```

### 3) Ensure whether a mock method is being called with required arguments or not

Mockito can ensure whether a mock method is being called with required arguments or not. It is done using the verify() method.

```java
//verify call to calcService is made or not with same arguments. Verify the behavior
verify(calcService).add(10.0, 20.0); //verify(calcService).add(20.0, 30.0);
```

### 4) Check on the number of calls that can be made on a particular method: times()

Mockito provides a special check on the number of calls that can be made on a particular method.

Suppose MathApplication should call the CalculatorService.serviceUsed() method only once, then it should not be able to call CalculatorService.serviceUsed() more than once.

```java
//limit the method call to 1, no less and no more calls are allowed
verify(calcService, times(1)).add(10.0, 20.0);

//default call count is 1
verify(calcService).subtract(20.0, 10.0);

//check if add function is called three times
verify(calcService, times(3)).add(10.0, 20.0);

//verify that method was never called on a mock
verify(calcService, never()).multiply(10.0,20.0);
```

### 5) Methods to vary the expected call counts: atLeast(), atLeastOnce(), atMost()

Mockito provides the following additional methods to vary the expected call counts.
* atLeast (int min) − expects min calls.
* atLeastOnce () − expects at least one call.
* atMost (int max) − expects max calls.

```java
//check a minimum 1 call count
verify(calcService, atLeastOnce()).subtract(20.0, 10.0);
//check if add function is called minimum 2 times
verify(calcService, atLeast(2)).add(10.0, 20.0);
//check if add function is called maximum 3 times
verify(calcService, atMost(3)).add(10.0,20.0);
```

### 6) Capability to a mock to throw exceptions

Mockito provides the capability to a mock to throw exceptions, so exception handling can be tested.

```java
@Test(expected = RuntimeException.class)
public void testAdd(){
  //add the behavior to throw exception
  doThrow(new RuntimeException("Add operation not implemented")).when(calcService).add(10.0,20.0);
}
```

### 7 Takes care of the order of method calls

Mockito provides Inorder class which takes care of the order of method calls that the mock is going to make in due course of its action.

```java
//create an inOrder verifier for a single mock
InOrder inOrder = inOrder(calcService);

//following will make sure that add is first called then subtract is called.
inOrder.verify(calcService).add(20.0,10.0);
inOrder.verify(calcService).subtract(20.0,10.0);
```

### 8) Answer interface

Mockito provides a Answer interface which allows stubbing with generic interface.

```java
@Test
public void testAdd(){
   //add the behavior to add numbers
   when(calcService.add(20.0,10.0)).thenAnswer(new Answer<Double>() {
      @Override
      public Double answer(InvocationOnMock invocation) throws Throwable {
         //get the arguments passed to mock
         Object[] args = invocation.getArguments();

         //get the mock 
         Object mock = invocation.getMock();	

         //return the result
         return 30.0;
      }
   });

   //test the add functionality
   Assert.assertEquals(mathApplication.add(20.0, 10.0),30.0,0);
}
```

### 9) Actual method of real object is called: spy()

Mockito provides option to create spy on real objects. When spy is called, then actual method of real object is called.

```java
//create a spy on actual object
calcService = spy(calculator);

//perform operation on real object
//test the add functionality
Assert.assertEquals(mathApplication.add(20.0, 10.0),30.0,0);
```

```java
import static org.mockito.Mockito.spy;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

// @RunWith attaches a runner with the test class to initialize the test data
@RunWith(MockitoJUnitRunner.class)
public class MathApplicationTester {
	
   private MathApplication mathApplication;
   private CalculatorService calcService;

   @Before
   public void setUp(){
      mathApplication = new MathApplication();
      Calculator calculator = new Calculator();
      calcService = spy(calculator);
      mathApplication.setCalculatorService(calcService);	     
   }

   @Test
   public void testAdd(){

      //perform operation on real object
      //test the add functionality
      Assert.assertEquals(mathApplication.add(20.0, 10.0),30.0,0);
   }

   class Calculator implements CalculatorService {
      @Override
      public double add(double input1, double input2) {
         return input1 + input2;
      }

      @Override
      public double subtract(double input1, double input2) {
         throw new UnsupportedOperationException("Method not implemented yet!");
      }

      @Override
      public double multiply(double input1, double input2) {
         throw new UnsupportedOperationException("Method not implemented yet!");
      }

      @Override
      public double divide(double input1, double input2) {
         throw new UnsupportedOperationException("Method not implemented yet!");
      }
   }
}
```

### 10) Reset a mock so that it can be reused later: reset()

Mockito provides the capability to a reset a mock so that it can be reused later. Take a look at the following code snippet.

```java
//reset mock
reset(calcService);
```

Here we've reset mock object. MathApplication makes use of calcService and after reset the mock, using mocked method will fail the test.

```java
@Test
public void testAddAndSubtract(){
   //add the behavior to add numbers
   when(calcService.add(20.0,10.0)).thenReturn(30.0);

   //test the add functionality
   Assert.assertEquals(mathApplication.add(20.0, 10.0),30.0,0);

   //reset the mock
   reset(calcService);

   //test the add functionality after resetting the mock
   Assert.assertEquals(mathApplication.add(20.0, 10.0),30.0,0);
}
```

### 11) Behavior Driven Development in Mockito

Behavior Driven Development is a style of writing tests uses given, when and then format as test methods. Mockito provides special methods to do so. Take a look at the following code snippet.

```java
//Given
given(calcService.add(20.0,10.0)).willReturn(30.0);

//when
double result = calcService.add(20.0,10.0);

//then
Assert.assertEquals(result,30.0,0);
```

Here we're using given method of BDDMockito class instead of when method of mockito.

```java
// @RunWith attaches a runner with the test class to initialize the test data
@RunWith(MockitoJUnitRunner.class)
public class MathApplicationTester {
	
   private MathApplication mathApplication;
   private CalculatorService calcService;

   @Before
   public void setUp(){
      mathApplication = new MathApplication();
      calcService = mock(CalculatorService.class);
      mathApplication.setCalculatorService(calcService);
   }

   @Test
   public void testAdd(){

      //Given
      given(calcService.add(20.0,10.0)).willReturn(30.0);

      //when
      double result = calcService.add(20.0,10.0);

      //then
      Assert.assertEquals(result,30.0,0);   
   }
}
```

### 12) Timeout option to test if a method is called within stipulated time frame

Mockito provides a special Timeout option to test if a method is called within stipulated time frame.

```java
//passes when add() is called within 100 ms.
verify(calcService,timeout(100)).add(20.0,10.0);
```

```java
@Test
public void testAddAndSubtract(){

    //add the behavior to add numbers
    when(calcService.add(20.0,10.0)).thenReturn(30.0);

    //subtract the behavior to subtract numbers
    when(calcService.subtract(20.0,10.0)).thenReturn(10.0);

    //test the subtract functionality
    Assert.assertEquals(mathApplication.subtract(20.0, 10.0),10.0,0);

    //test the add functionality
    Assert.assertEquals(mathApplication.add(20.0, 10.0),30.0,0);

    //verify call to add method to be completed within 100 ms
    verify(calcService, timeout(100)).add(20.0,10.0);

    //invocation count can be added to ensure multiplication invocations
    //can be checked within given timeframe
    verify(calcService, timeout(100).times(1)).subtract(20.0,10.0);
}
```
