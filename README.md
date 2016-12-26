# Just a simple spring application
 
## Request

### Add transaction:
```
   curl -H "Content-Type: application/json" -X PUT -d '{"amount":10,"type":"cc"}' {URL}/transaction/{transaction-id}
   curl -H "Content-Type: application/json" -X PUT -d '{"amount":10,"type":"dc", "parentId":1}' {URL}/transaction/{transaction-id}
```

### GET transaction:
```
   curl -H "Content-Type: application/json" -X GET {URL}/transaction/{transaction-id}
```

### GET all transaction of same type
```
   curl -H "Content-Type: application/json" -X GET {URL}/transaction/types/{transaction-type}
```

### GET sum of all transactions linked from parent downward
```
   curl -H "Content-Type: application/json" -X GET {URL}/transaction/sum/{transaction-id}
```


## Examples:
```
    PUT transaction:
    curl -H "Content-Type: application/json" -X PUT -d '{"amount":10,"type":"cc"}' {URL}/transaction/1
    curl -H "Content-Type: application/json" -X PUT -d '{"amount":10,"type":"cc", "parentId":1}' {URL}/transaction/2
    
    GET transaction:
    curl -H "Content-Type: application/json" -X GET {URL}/transaction/1
    curl -H "Content-Type: application/json" -X GET {URL}/transaction/2
    
    GET all transactions for same type:
    curl -H "Content-Type: application/json" -X GET {URL}/transaction/types/cc
    
    GET sum of all transactions linked from parent downward:
    curl -H "Content-Type: application/json" -X GET {URL}/transaction/sum/1
```
