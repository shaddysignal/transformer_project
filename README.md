# Transform project

## Transform API
Access point is `/api/transform`. Accepts:
```
{
  "input": String,
  "transformers": [{"transformerId": String, "parameters": {}}]
}
```

Currently implemented transformers:
1. Remove. id: `remove`, should come with `regexPattern` parameter.
1. Replace. id: `replace`, should come with `regexPattern` and `with` parameters.
1. Uppercase. id: `uppercase`
1. Lowercase. id: `lowercase`

## Reports
Possible to get all executions at `/api/execution-list`. Accepts:
```
{
  "from": Date(yyyy-mm-dd)
  "to": Date(yyyy-mm-dd)
}
```
To get statistics for each transformer id, call `/api/execution-report` (to get csv file instead add `.csv` suffix). Accepts:
```
{
  "from": Date(yyyy-mm-dd)
  "to": Date(yyyy-mm-dd)
}
```
