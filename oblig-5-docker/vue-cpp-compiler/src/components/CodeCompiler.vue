<template>
    <div class="container">
        <h1>Monster Code Compiler 3000</h1>
        <textarea class="code-input" id="code-input" placeholder="Write code here..." onfocus="this.placeholder = ''" onblur="this.placeholder = 'Write code here...'">
            
        </textarea>

        <div class="button-container">
            <button id="compile-button" class="btn btn-primary btn-lg btn-block" @click="compileCode()">Compile</button>
            <div id="spinner" class="spinner-border text-dark" role="status">
                <span class="sr-only">Loading...</span>
            </div>
        </div>

        <textarea readonly class="code-output" id="code-output" placeholder="Output comes here..." onfocus="this.placeholder = ''" onblur="this.placeholder = 'Output comes here...'"></textarea>
    </div>
</template>

<script>

export default {
  name: 'CodeCompiler',

  methods: {
    compileCode(){

        let compileButton = document.querySelector("#compile-button");
        let compileSpinner = document.querySelector("#spinner");
        let code = {
            "code" : document.querySelector("#code-input").value
        }

        compileButton.style.backgroundColor = "#d8d8d8";
        compileSpinner.style.display = "block";

        console.log(document.querySelector("#code-input").value);


        // Simple POST request with a JSON body using fetch
        const requestOptions = {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(code)
        };

        fetch("http://localhost:8080/compile", requestOptions)
        .then(response => response.json())
        .then(data => {
            console.log(data);
            document.querySelector("#code-output").value = data.code;
        })
        .finally(() => {
            compileButton.style.backgroundColor = "#4b7c8b";
            compileSpinner.style.display = "none";
        });
    }
  }
}

</script>

<style>
    .container{
        width: 100vw;
        height: 100vh;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
    }

     
    .button-container{
        width: 100%;
        display: flex;
    }
    .button-container button{
        width: 25%;
        background-color: #4b7c8b;
        margin-right: 1rem;
        outline: none;
    }

    .button-container #spinner {
        display: none;
        margin-top: 0.4rem;
    }

    .button-container button:hover{
        background-color: #3f6875;
    }


    .code-input{
        color: white;
        background-color: #1d1e22;
        margin: 10px;
        width: 100%;
        min-height: 50%;
        resize: none;
        border-radius: 8px;
        border: 1px solid #ddd;
        padding: 1rem;
        box-shadow: inset 0 0 0.25rem #ddd;
    }

    .code-output{
        color: white;
        background-color: #1d1e22;
        margin: 10px;
        width: 100%;
        min-height: 20%;
        resize: none;
        border-radius: 8px;
        border: 1px solid #ddd;
        padding: 1rem;
        box-shadow: inset 0 0 0.25rem #ddd;
    }

    .code-input:focus, .code-output:focus{
        outline: none;
        border: 1px solid #ddd;
        box-shadow: inset 0 0 0.5rem#ddd;
    }

    .code-input[placeholder], .code-output[placeholder]{ 
        font-size: 1.2rem;
    }

</style>


