Feature: GET: Automated Demo Tests

@task2
Scenario: GET: Test the Demo app
  When I Post the data with groupname
  |GroupName|
  |ABC  |
  |PQR  |
  Then I Post the data to resource api
  |GroupName|ResourceName|
  |ABC  |A               |
  |ABC  |B               |
  |PQR  |C               |
  Then I hit the Get resource API
  |GroupName|ResourceName|
  |ABC  |A               |
  |PQR  |C               |
  Then I hit the Get Resource List API and verify the Data
  |GroupName|
  |ABC  |
  |PQR  |