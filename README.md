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
