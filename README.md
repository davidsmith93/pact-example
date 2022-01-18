# PACT Example

## Contract
```
POST - /example

Request:
{
    "request_string": "string",
    "request_number": 0,
    "request_object": {
        "request_boolean": true
    },
    "request_array":[
        0.0
    ]
}

Response:
200
{
    "response_string": "string",
    "response_number": 0,
    "response_object": {
        "response_boolean": true
    },
    "response_array":[
        0.0
    ]
}

400
{
    "error_enum": "VALIDATION | UNKNOWN",
    "error_string": "string"
}
```