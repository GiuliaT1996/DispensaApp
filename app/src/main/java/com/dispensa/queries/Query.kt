package com.dispensa.queries

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplifyframework.api.ApiException
import com.amplifyframework.api.graphql.GraphQLResponse
import com.amplifyframework.api.graphql.PaginatedResult
import com.amplifyframework.api.graphql.model.ModelMutation
import com.amplifyframework.api.graphql.model.ModelQuery
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.Consumer
import com.amplifyframework.datastore.generated.model.Dispensa
import kotlinx.coroutines.launch

class Query  : ViewModel(){

    // class Query constructor()

    fun getList() : List<Dispensa> {
        val dispense: List<Dispensa> = listOf()
        viewModelScope.launch {
            try {
                Amplify.API.query(
                        ModelQuery.list(Dispensa::class.java),
                        Consumer { response: GraphQLResponse<PaginatedResult<Dispensa?>> ->
                            Log.i("Amplify", "Recuperati Risultati")
                            for (d in response.data) {
                                dispense.plus(d)
                                Log.i("Amplify", "Recuperato: " + if (d != null && d.name != null) d.name else null
                                        + " id = " +  if (d != null && d.id != null) d.id else null)
                            }
                        },
                        Consumer { error: ApiException? -> Log.e("Amplify", "Query failure", error) }
                )
            } catch(e : ApiException) {
                Log.e("Amplify", "Query failure", e)
            }
        }
        return dispense

    }

    fun findById(id: String, dis : Dispensa) : Dispensa {
        var d : Dispensa = dis
        viewModelScope.launch {
            try{
                Amplify.API.query(ModelQuery.get(Dispensa::class.java, id),
                        { response: GraphQLResponse<Dispensa> ->  Log.i("Amplify", "Recuperati Risultati")
                            d = response.data},
                        { error: ApiException? -> Log.e("Amplify", "Query failure", error) })

            } catch(e : ApiException) {
                Log.e("Amplify", "Query failure", e)
            }
        }
        return d

    }

    fun create(item : Dispensa) {
        viewModelScope.launch {
            Amplify.API.mutate(
                    ModelMutation.create(item),
                    Consumer { response: GraphQLResponse<Dispensa> ->
                        Log.i("Amplify", "Created")
                        if (response.hasErrors()) {
                            Log.e("Amplify", response.errors[0].message)
                        } else {
                            Log.i("Amplify", "Created Note with id: " + response.data.id)
                        }
                    },
                    Consumer { error: ApiException? -> Log.e("Amplify", "Create failed", error) }
            )
        }
    }

    fun deleteAll(dispense: List<Dispensa>) {
        viewModelScope.launch {
            for (d in dispense) {
                Amplify.API.query(
                        ModelMutation.delete(d),
                        { response: GraphQLResponse<Dispensa>? -> Log.i("Amplify", "Cancellato con successo") }
                ) { error: ApiException? -> Log.e("Amplify", "Query failure", error) }
            }
        }
    }

    fun deleteById(id : String, dis : Dispensa) {

        val d = findById(id, dis)

        viewModelScope.launch {

            Amplify.API.query(
                    ModelMutation.delete(d),
                    { response: GraphQLResponse<Dispensa>? -> Log.i("Amplify", "Cancellato con successo") }
            ) { error: ApiException? -> Log.e("Amplify", "Query failure", error) }
        }
    }

    fun getNull(dis: Dispensa){
        var d : Dispensa = dis
        viewModelScope.launch {
            try {
                Amplify.API.mutate(
                        ModelMutation.delete(d),
                        Consumer { response: GraphQLResponse<Dispensa> ->
                            Log.i("Amplify", "Deleted")
                            if (response.hasErrors()) {
                                Log.e("Amplify", response.errors[0].message)
                            } else {
                                Log.i("Amplify", "Deleted Note with id: ")
                            }
                        },
                        Consumer { error: ApiException? -> Log.e("Amplify", "Delet failed", error) }
                )
            } catch(e : ApiException) {
                Log.e("Amplify", "Query failure", e)
            }
        }

    }
}