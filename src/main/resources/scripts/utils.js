<script>
    function isEmpty(str) {
        return (!str || 0 === str.length);
    }

    function parseBoolean(string) {
        switch (string.toLowerCase().trim()) {
            case "true": case "yes": case "1": return true;
            case "false": case "no": case "0": case null: return false;
            default: return Boolean(string);
        }
    }

</script>